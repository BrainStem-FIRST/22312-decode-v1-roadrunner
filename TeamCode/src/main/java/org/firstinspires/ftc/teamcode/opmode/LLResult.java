package org.firstinspires.ftc.teamcode.opmode;

public class LLResult {
    private boolean valid;
    private double tx;
    private double ty;
    private double ta;

    public LLResult(boolean valid, double tx, double ty, double ta) {
        this.valid = valid;
        this.tx = tx;
        this.ty = ty;
        this.ta = ta;
    }

    public boolean isValid() {
        return valid;
    }

    public double getTx() {
        return tx;
    }

    public double getTy() {
        return ty;
    }

    public double getTa() {
        return ta;
    }

    // Added to prevent crashes if code calls getBotpose() even if we don't use it
    public org.firstinspires.ftc.robotcore.external.navigation.Pose3D getBotpose() {
        return null;
    }
}