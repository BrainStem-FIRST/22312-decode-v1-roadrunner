package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp (Refactored & Button Corrected)")
public class BrainSTEMTeleOp extends LinearOpMode {

    // Subsystems
    private Drive drive;
    private Intake intake;
    private Shooter shooter;
    private Indexer indexer;
    private Transfer transfer;

    // --- State Tracking for "WasPressed" Logic ---
    // We must track the previous state of buttons to detect a "Click" vs a "Hold".
    private boolean previousAState = false;       // Shooter Toggle
    private boolean previousRBState = false;      // Indexer Advance (StoS Bias)
    private boolean previousLBState = false;      // Indexer Reverse (StoS Bias)
    private boolean previousYState = false;       // Indexer Advance (CtoC Bias)
    private boolean previousBState = false;       // Indexer Reverse (CtoC Bias)
    // Note: Transfer (X) logic in original was a "Hold", so we don't need state for X.

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize Subsystems
        drive = new Drive(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        indexer = new Indexer(hardwareMap);
        transfer = new Transfer(hardwareMap);

        telemetry.addLine("Robot Initialized. Ready for Start!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ============================================================
            // DRIVER 1 (D1): CHASSIS & INTAKE
            // ============================================================

            // --- DRIVE (D1) ---
            drive.drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x);

            // --- INTAKE (D1 + D2 Assist) ---
            // D1 Right Trigger OR D2 Right Trigger -> INTAKE
            if (gamepad1.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD ||
                    gamepad2.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runIntake();
            }
            // D1 Left Trigger -> OUTTAKE
            else if (gamepad1.left_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runOuttake();
            }
            else {
                intake.stop();
            }


            // ============================================================
            // DRIVER 2 (D2): MANIPULATOR (SHOOTER, INDEXER, TRANSFER)
            // ============================================================

            // --- SHOOTER (D2 Button A) ---
            // Logic: Smart Toggle (Click A to On, Click A to Off)
            boolean currentAState = gamepad2.a;
            if (currentAState && !previousAState) {
                shooter.toggle();
            }
            previousAState = currentAState;


            // --- INDEXER (D2 Bumpers, Y, B) ---
            // We strictly implement "Rising Edge" (WasPressed) logic here so it
            // only moves ONE step per click, matching original behavior.

            boolean currentRBState = gamepad2.right_bumper;
            boolean currentLBState = gamepad2.left_bumper;
            boolean currentYState = gamepad2.y;
            boolean currentBState = gamepad2.b;

            // 1. Right Bumper (Advance - Shoot Bias)
            if (currentRBState && !previousRBState) {
                indexer.handleRightBumper();
            }

            // 2. Y Button (Advance - Collect Bias)
            if (currentYState && !previousYState) {
                indexer.handleYButton();
            }

            // 3. Left Bumper (Reverse - Shoot Bias)
            if (currentLBState && !previousLBState) {
                indexer.handleLeftBumper();
            }

            // 4. B Button (Reverse - Collect Bias)
            if (currentBState && !previousBState) {
                indexer.handleBButton();
            }

            // Update previous states for next loop
            previousRBState = currentRBState;
            previousLBState = currentLBState;
            previousYState = currentYState;
            previousBState = currentBState;


            // --- INDEXER HOME (D2 D-Pad) ---
            // Original logic had this nested in B, but D-Pad Right is generally a standalone override.
            if (gamepad2.dpad_right) {
                indexer.goToHome();
            }

            // CRITICAL: Update the Indexer Loop
            indexer.update();


            // --- TRANSFER (D2 Button X) ---
            // Original logic: Fire while holding X.
            if (gamepad2.x) {
                // Safety check: only fire if Indexer is aligned (Even state or 0)
                if (indexer.isAtShootPosition() && Math.abs(indexer.getIndexerError()) <= 3) {
                    transfer.fire();
                }
            } else {
                transfer.home();
            }


            // --- TELEMETRY ---
            telemetry.addData("Indexer State", indexer.currentState);
            telemetry.addData("Shooter On?", shooter.isShooting());
            telemetry.update();
        }
    }
}