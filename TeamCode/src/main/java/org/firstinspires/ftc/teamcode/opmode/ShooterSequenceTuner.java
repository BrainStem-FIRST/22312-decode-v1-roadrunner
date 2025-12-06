package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TOOL: Shooter Sequence Tuner", group = "Tools")
public class ShooterSequenceTuner extends LinearOpMode {

    private Shooter shooter;
    private Indexer indexer;
    private Transfer transfer;
    private ThreeBallRapidFire rapidFire;

    // Menu State
    private int selectedParameter = 0; // 0=Settle, 1=Up, 2=Down
    private String[] paramNames = {"Indexer Settle", "Transfer UP", "Transfer DOWN"};

    // Previous button states for toggling
    boolean lastUp, lastDown, lastLeft, lastRight, lastRB, lastB;

    @Override
    public void runOpMode() {
        // Initialize Hardware
        shooter = new Shooter(hardwareMap);
        indexer = new Indexer(hardwareMap, gamepad2);
        transfer = new Transfer(hardwareMap);
        rapidFire = new ThreeBallRapidFire(shooter, indexer, transfer);

        telemetry.addLine("Initialized.");
        telemetry.addLine("Use D-Pad UP/DOWN to select parameter.");
        telemetry.addLine("Use D-Pad LEFT/RIGHT to adjust time.");
        telemetry.addLine("Press Right Bumper (RB) to FIRE sequence.");
        telemetry.addLine("Press B to toggle Shooter motor.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // --- UPDATES ---
            indexer.update();
            rapidFire.update(); // Keeps the state machine ticking

            // --- CONTROLS ---

            // 1. Menu Selection (Up/Down)
            if (gamepad1.dpad_up && !lastUp) {
                selectedParameter--;
                if (selectedParameter < 0) selectedParameter = 2;
            }
            if (gamepad1.dpad_down && !lastDown) {
                selectedParameter++;
                if (selectedParameter > 2) selectedParameter = 0;
            }

            // 2. Value Adjustment (Left/Right)
            long increment = 10; // Milliseconds
            if (gamepad1.dpad_right && !lastRight) {
                adjustParameter(selectedParameter, increment);
            }
            if (gamepad1.dpad_left && !lastLeft) {
                adjustParameter(selectedParameter, -increment);
            }

            // 3. Action Triggers
            // FIRE SEQUENCE (Right Bumper)
            if (gamepad1.right_bumper && !lastRB) {
                indexer.handleRapidFire();
                sleep(250);
                rapidFire.startSequence();
            }

            // Toggle Shooter Motor (B)
            if (gamepad1.b && !lastB) {
                shooter.toggle();
            }

            // Manual Reset/Abort (Y)
            if (gamepad1.y) {
                rapidFire.abort();
                indexer.goToHome();
            }

            // Update button history
            lastUp = gamepad1.dpad_up;
            lastDown = gamepad1.dpad_down;
            lastLeft = gamepad1.dpad_left;
            lastRight = gamepad1.dpad_right;
            lastRB = gamepad1.right_bumper;
            lastB = gamepad1.b;

            // --- DISPLAY ---
            telemetry.addLine("=== TUNING MODE ===");
            displayParam(0, rapidFire.settleTimeMs);
            displayParam(1, rapidFire.upTimeMs);
            displayParam(2, rapidFire.downTimeMs);

            telemetry.addLine("\n--- STATUS ---");
            telemetry.addData("Sequence State", rapidFire.getStatus());
            telemetry.addData("Is Running", rapidFire.isRunning());
            telemetry.addData("Shooter RPM", "%.0f / %.0f", shooter.getCurrentRPM(), shooter.getTargetRPM());

            telemetry.update();
        }
    }

    private void adjustParameter(int param, long amount) {
        switch (param) {
            case 0:
                rapidFire.settleTimeMs += amount;
                if (rapidFire.settleTimeMs < 0) rapidFire.settleTimeMs = 0;
                break;
            case 1:
                rapidFire.upTimeMs += amount;
                if (rapidFire.upTimeMs < 0) rapidFire.upTimeMs = 0;
                break;
            case 2:
                rapidFire.downTimeMs += amount;
                if (rapidFire.downTimeMs < 0) rapidFire.downTimeMs = 0;
                break;
        }
    }

    private void displayParam(int index, long value) {
        String prefix = (index == selectedParameter) ? ">> " : "   ";
        telemetry.addData(prefix + paramNames[index], value + " ms");
    }
}