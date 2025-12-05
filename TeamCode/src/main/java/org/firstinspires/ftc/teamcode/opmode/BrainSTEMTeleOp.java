package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// Note: No @TeleOp annotation here. This is the logic engine only.
// It is ABSTRACT because it needs RedTeleOp or BlueTeleOp to tell it the offset.
public abstract class BrainSTEMTeleOp extends LinearOpMode {

    // Abstract method: Child classes must implement this to tell us the offset
    public abstract double getVisionOffset();

    private Drive drive;
    private Intake intake;
    private Shooter shooter;
    private Indexer indexer;
    private Transfer transfer;
    private Vision vision;
    private ThreeBallRapidFire rapidFire;

    // Driver 2 Toggles
    private boolean previousAState = false;
    private boolean previousRBState = false;
    private boolean previousLBState = false;
    private boolean previousYState = false;
    private boolean previousBState = false;

    // Driver 1 Toggles
    private boolean previousD1RBState = false;


    @Override
    public void runOpMode() throws InterruptedException {
        // 1. SET ALLIANCE OFFSET FIRST
        // This method calls into RedTeleOp.java or BlueTeleOp.java to get the number.
        RobotConstants.VISION_AIM_OFFSET = getVisionOffset();

        // 2. Initialize Hardware Subsystems
        drive = new Drive(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        indexer = new Indexer(hardwareMap, gamepad2);
        transfer = new Transfer(hardwareMap);
        vision = new Vision(hardwareMap);

        // 3. Initialize Logic Subsystems
        rapidFire = new ThreeBallRapidFire(shooter, indexer, transfer);

        telemetry.addLine("Robot Initialized.");
        telemetry.addData("Alliance Offset", RobotConstants.VISION_AIM_OFFSET);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // --- 1. SENSOR & LOGIC UPDATE ---
            // These just run the background logic. They do not return values.
            vision.update();
            rapidFire.update();


            // ============================================================
            // DRIVER 1 (D1): CHASSIS, INTAKE, & AUTO FIRE
            // ============================================================

            double forward = -gamepad1.left_stick_y;
            double strafe  = gamepad1.left_stick_x;
            double turn    = -gamepad1.right_stick_x;

            // Vision Override (Left Bumper)
            if (gamepad1.left_bumper) {
                if (vision.hasTarget()) {
                    turn = vision.getAlignTurnPower();
                }
            }

            drive.drive(strafe, forward, turn);

            // Intake Controls (Triggers)
            if (gamepad1.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD ||
                    gamepad2.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runIntake();
            }
            else if (gamepad1.left_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD ||
                    gamepad2.left_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runOuttake();
            }
            else {
                intake.stop();
            }


            if (gamepad2.x)
                indexer.flickTimer.reset();

            // --- 3-BALL RAPID FIRE (Right Bumper) ---
            boolean currentD1RBState = gamepad1.right_bumper;
            if (currentD1RBState && !previousD1RBState) {
                indexer.rapidFireRB();
                sleep(350);
                rapidFire.startSequence();
            }
            previousD1RBState = currentD1RBState;

            // --- EMERGENCY STOP (Y Button) ---
            // If Driver 1 hits Y, we abort the sequence immediately.
            if (gamepad1.y) {
                rapidFire.abort();
            }

            // ============================================================
            // DRIVER 2 (D2): MANIPULATOR
            // ============================================================

            // Shooter Toggle
            boolean currentAState = gamepad2.a;
            if (currentAState && !previousAState) {
                shooter.toggle();
            }
            previousAState = currentAState;

            // Shooter Speed Selection
            if (gamepad2.dpad_up) shooter.setTargetRPM(RobotConstants.SHOOTER_RPM_FAR);
            if (gamepad2.dpad_down) shooter.setTargetRPM(RobotConstants.SHOOTER_RPM_NEAR);

            // Indexer Manual Controls
            boolean currentRBState = gamepad2.right_bumper;
            boolean currentLBState = gamepad2.left_bumper;
            boolean currentYState = gamepad2.y;
            boolean currentBState = gamepad2.b;

            if (gamepad2.x && shooter.error <= 50 && indexer.isAtShootPosition() && Math.abs(indexer.getIndexerError()) < 3) {
                transfer.fire();
            } else {
                transfer.home();
            }
            telemetry.addData("shooter error", shooter.error);
            telemetry.addData("indexer error", indexer.getIndexerError());

            if (currentRBState && !previousRBState) indexer.handleRightBumper();
            if (currentYState && !previousYState)   indexer.handleYButton();
            if (currentLBState && !previousLBState) indexer.handleLeftBumper();
            if (currentBState && !previousBState)   indexer.handleBButton();

            // --- SAFETY OVERRIDE ---
            // If Driver 2 touches ANY indexer button, abort the Auto Sequence.
            // This prevents "fighting" the automation.
            if ((currentRBState && !previousRBState) ||
                    (currentYState && !previousYState)   ||
                    (currentLBState && !previousLBState) ||
                    (currentBState && !previousBState)) {
                rapidFire.abort();
            }

            previousRBState = currentRBState;
            previousLBState = currentLBState;
            previousYState = currentYState;
            previousBState = currentBState;

            indexer.update();

            // --- MANUAL FIRE LOGIC ---

            // 1. Calculate Live Interlocks (Do not trust stale public variables)
            // We calculate the error right here, right now, to ensure safety.
            double currentShooterError = Math.abs(shooter.getTargetRPM() - shooter.getCurrentRPM());
            double currentIndexerError = Math.abs(indexer.getIndexerError());

            // 2. Define "Ready" Conditions
            // Shooter: Must be spinning (isShooting) AND within 50 RPM of target
            boolean isShooterReady = shooter.isShooting() && (currentShooterError <= 50);

            // Indexer: Must be in a "Shoot" (Even) position AND physically aligned
            // Note: I widened the tolerance to 10 ticks (was 3) to make the button more responsive.
            boolean isIndexerReady = indexer.isAtShootPosition() && (currentIndexerError <= 10);

            // 3. Logic Gate
            // IF Rapid Fire is running -> Automation controls the servo (Ignore Manual)
            // IF Rapid Fire is NOT running -> Driver 2 controls the servo (Manual)
//            if (rapidFire.isRunning()) {
//            }
//            else {
//                // MANUAL CONTROL:
//                // Only fire if Safety Interlocks pass AND Button is pressed
//                if (gamepad2.x && isShooterReady && isIndexerReady) {
//                    transfer.fire();
//                } else {
//                    // If not firing manually, keep the transfer retracted
//                    transfer.home();
//                }
//            }

            // --- TELEMETRY ---
            vision.printInfo(telemetry);
            telemetry.addData("Offset", RobotConstants.VISION_AIM_OFFSET);
            if (shooter.isShooting()) {
                telemetry.addData("Shooter", "ON | RPM: %.0f", shooter.getCurrentRPM());
            } else {
                telemetry.addData("Shooter", "OFF");
            }
            telemetry.addData("Auto Fire Status", rapidFire.getStatus());
            telemetry.addData("is shooter ready", isShooterReady);
            telemetry.addData("is indexer ready", isIndexerReady);
            telemetry.update();

        }

        vision.stop();
    }
}