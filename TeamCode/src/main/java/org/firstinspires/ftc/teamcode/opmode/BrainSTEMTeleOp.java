package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp (Refactored & Vision)")
public class BrainSTEMTeleOp extends LinearOpMode {

    private Drive drive;
    private Intake intake;
    private Shooter shooter;
    private Indexer indexer;
    private Transfer transfer;
    private Vision vision;

    private boolean previousAState = false;
    private boolean previousRBState = false;
    private boolean previousLBState = false;
    private boolean previousYState = false;
    private boolean previousBState = false;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Drive(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        indexer = new Indexer(hardwareMap);
        transfer = new Transfer(hardwareMap);
        vision = new Vision(hardwareMap);

        telemetry.addLine("Robot Initialized. Ready for Start!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // --- 1. SENSOR UPDATE ---
            vision.update();

            // ============================================================
            // DRIVER 1 (D1): CHASSIS & INTAKE
            // ============================================================

            double forward = -gamepad1.left_stick_y;
            double strafe  = gamepad1.left_stick_x;
            double turn    = -gamepad1.right_stick_x;

            // Vision Override
            if (gamepad1.left_bumper) {
                if (vision.hasTarget()) {
                    turn = vision.getAlignTurnPower();
                    // We use printInfo below for detailed debug
                } else {
                    // Keep manual control if target lost? Or stop?
                    // Current: Manual control overrides if no target found
                }
            }

            drive.drive(strafe, forward, turn);

            if (gamepad1.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD ||
                    gamepad2.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runIntake();
            }
            else if (gamepad1.left_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runOuttake();
            }
            else {
                intake.stop();
            }

            // ============================================================
            // DRIVER 2 (D2): MANIPULATOR
            // ============================================================

            boolean currentAState = gamepad2.a;
            if (currentAState && !previousAState) {
                shooter.toggle();
            }
            previousAState = currentAState;

            if (gamepad2.dpad_up) shooter.setTargetRPM(RobotConstants.SHOOTER_RPM_FAR);
            if (gamepad2.dpad_down) shooter.setTargetRPM(RobotConstants.SHOOTER_RPM_NEAR);

            boolean currentRBState = gamepad2.right_bumper;
            boolean currentLBState = gamepad2.left_bumper;
            boolean currentYState = gamepad2.y;
            boolean currentBState = gamepad2.b;

            if (currentRBState && !previousRBState) indexer.handleRightBumper();
            if (currentYState && !previousYState)   indexer.handleYButton();
            if (currentLBState && !previousLBState) indexer.handleLeftBumper();
            if (currentBState && !previousBState)   indexer.handleBButton();

            previousRBState = currentRBState;
            previousLBState = currentLBState;
            previousYState = currentYState;
            previousBState = currentBState;

            if (gamepad2.dpad_right) indexer.goToHome();
            indexer.update();

            if (gamepad2.x) {
                if (indexer.isAtShootPosition() && Math.abs(indexer.getIndexerError()) <= 3) {
                    transfer.fire();
                }
            } else {
                transfer.home();
            }

            // --- TELEMETRY ---
            // Display the Limelight Debug Info (Sister Team Style)
            vision.printInfo(telemetry);

            telemetry.addData("Indexer", indexer.currentState);
            if (shooter.isShooting()) {
                telemetry.addData("Shooter", "ON | RPM: %.0f", shooter.getCurrentRPM());
            } else {
                telemetry.addData("Shooter", "OFF");
            }
            telemetry.update();
        }

        vision.stop();
    }
}