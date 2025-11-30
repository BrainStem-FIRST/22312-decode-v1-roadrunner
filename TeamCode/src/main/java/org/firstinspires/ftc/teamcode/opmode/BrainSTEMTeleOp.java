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
        indexer = new Indexer(hardwareMap);
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

            // --- 3-BALL RAPID FIRE (Right Bumper) ---
            boolean currentD1RBState = gamepad1.right_bumper;
            if (currentD1RBState && !previousD1RBState) {
                indexer.handleRightBumper();
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
            // Check if robot is physically ready to fire
            boolean indexerReady = indexer.isAtShootPosition() && Math.abs(indexer.getIndexerError()) <= 3;
            boolean shooterReady = Math.abs(shooter.error) <= 50;

            // LOGIC CHECK: Is the Auto Sequence currently running?
            if (!rapidFire.isRunning()) {
                // If Auto is NOT running, Driver 2 has manual control of the servo.
                if (indexerReady && shooterReady && (gamepad2.x || gamepad2.dpad_right)) {
                    transfer.fire();
                } else {
                    transfer.home();
                }
            }
            // If Auto IS running, we ignore Driver 2's X button so the servo doesn't stutter.

            // --- TELEMETRY ---
            vision.printInfo(telemetry);
            telemetry.addData("Offset", RobotConstants.VISION_AIM_OFFSET);
            if (shooter.isShooting()) {
                telemetry.addData("Shooter", "ON | RPM: %.0f", shooter.getCurrentRPM());
            } else {
                telemetry.addData("Shooter", "OFF");
            }
            telemetry.addData("Auto Fire Status", rapidFire.getStatus());
            telemetry.update();
        }

        vision.stop();
    }
}