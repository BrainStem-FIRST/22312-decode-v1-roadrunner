package org.firstinspires.ftc.teamcode.opmode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * A "Shim" class that mimics the official Limelight3A hardware class.
 * UPDATED: Now auto-scans multiple IPs to find the camera.
 */
public class Limelight3A {

    // We will try these addresses in order until one works
    private static final String[] POSSIBLE_URLS = {
            "http://172.29.0.1:5807/results",   // Standard Factory IP
            "http://172.29.0.31:5807/results",  // Your Control Hub Assigned IP
            "http://limelight:5807/results"     // Hostname lookup
    };

    private int activeUrlIndex = 0;
    private long lastSwitchTime = 0;

    public void start() {}
    public void pipelineSwitch(int id) {}

    public LLResult getLatestResult() {
        // Try to get data from the current target URL
        LLResult result = fetchFromUrl(POSSIBLE_URLS[activeUrlIndex]);

        // If valid, great! Keep using this URL.
        if (result.isValid()) {
            return result;
        }

        // If invalid, and it's been >200ms, try the next URL in the list
        // This allows us to "Hunt" for the camera automatically
        if (System.currentTimeMillis() - lastSwitchTime > 200) {
            activeUrlIndex++;
            if (activeUrlIndex >= POSSIBLE_URLS.length) {
                activeUrlIndex = 0;
            }
            lastSwitchTime = System.currentTimeMillis();
        }

        // Return the empty result (will show False/0.0) until we lock on
        return result;
    }

    private LLResult fetchFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30); // Very fast timeout to scan quickly
            conn.setReadTimeout(30);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JSONObject json = new JSONObject(content.toString());

            // Parse JSON (Standard Limelight Format)
            JSONObject data = json;
            if (json.has("results")) data = json.getJSONObject("results");

            boolean valid = false;
            if (data.has("v")) valid = (data.getInt("v") == 1);
            else if (data.has("tv")) valid = (data.getInt("tv") == 1);

            // If not valid, return early
            if (!valid) return new LLResult(false, 0, 0, 0);

            double tx = data.optDouble("tx", 0.0);
            double ty = data.optDouble("ty", 0.0);
            double ta = data.optDouble("ta", 0.0);

            // Check for Fiducial Array (AprilTags)
            if (data.has("Fiducial") && data.getJSONArray("Fiducial").length() > 0) {
                JSONObject tag = data.getJSONArray("Fiducial").getJSONObject(0);
                tx = tag.optDouble("tx", tx);
                ty = tag.optDouble("ty", ty);
            }

            return new LLResult(true, tx, ty, ta);

        } catch (Exception e) {
            return new LLResult(false, 0, 0, 0);
        }
    }

    public void stop() {}
}