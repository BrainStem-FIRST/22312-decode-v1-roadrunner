package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Import all of our new subsystems!
import org.firstinspires.ftc.teamcode.opmode.Drive;
import org.firstinspires.ftc.teamcode.opmode.Intake;
import org.firstinspires.ftc.teamcode.opmode.Shooter;
import org.firstinspires.ftc.teamcode.opmode.Indexer;
import org.firstinspires.ftc.teamcode.opmode.Transfer;
// We also need our constants file
import org.firstinspires.ftc.teamcode.opmode.RobotConstants;

/**
 * This is the main refactored TeleOp file.
 * Its only job is to map gamepad inputs to subsystem "verb" methods.
 * All the complex logic is now inside the subsystem classes.
 */
@TeleOp(name = "TeleOp (Refactored w/ original shooter logic)")
public class BrainSTEMTeleOp extends LinearOpMode {

    // --- Subsystem Declarations ---
    private Drive drive;
    private Intake intake;
    private Shooter shooter;
    private Indexer indexer;
    private Transfer transfer;
    // private Sensors sensors; // Uncomment when Sensors.java is ready

    // --- State Variables ---
    // This is the original 'onOff' variable from your first file.
    private int onOff = 0; // STATE: Tracks the shooter motor toggle (1=on, 2=off, loops to 1).

    // NOTE: The original file used 'gamepad2.rightBumperWasPressed()'.
    // If this method doesn't exist in your FTC SDK version,
    // you will need to add "WasPressed" helper variables for the bumpers.


    @Override
    public void runOpMode() throws InterruptedException {

        // --- Initialization ---
        // Initialize all subsystems by passing them the hardware map
        drive = new Drive(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        indexer = new Indexer(hardwareMap);

        transfer = new Transfer(hardwareMap);
        // sensors = new Sensors(hardwareMap); // Uncomment when ready

        telemetry.addLine("Robot Initialized. Ready for Start!");
        telemetry.update();

        waitForStart();

        // --- MAIN TELEOP LOOP ---
        while (opModeIsActive()) {

            // --- SUBSYSTEM: Drive ---
            // Drive logic is unchanged
            drive.drive(
                    gamepad1.left_stick_x,
                    -gamepad1.left_stick_y, // Y stick is inverted
                    -gamepad1.right_stick_x
            );


            // --- SUBSYSTEM: Intake ---
            // This logic is much cleaner now
            if (gamepad1.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD ||
                    gamepad2.right_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runIntake();
            } else if (gamepad1.left_trigger > RobotConstants.INTAKE_TRIGGER_THRESHOLD) {
                intake.runOuttake();
            } else {
                intake.stop();
            }


            // --- SUBSYSTEM: Shooter ---
            // This is your original 'onOff' integer logic.
            // NOTE: This logic is sensitive to how long the 'A' button is held.
            // If the driver holds it, 'onOff' will count very fast.
            if (gamepad2.a) {
                onOff = onOff + 1;
            }

            // Now, we call the subsystem methods based on the 'onOff' state
            if (onOff == 1) {
                shooter.run(); // Tell the shooter subsystem to run
            }
            if (onOff == 2) {
                shooter.stop(); // Tell the shooter subsystem to stop
            }
            if (onOff > 2) {
                onOff = 1; // Reset the counter
            }


            // --- SUBSYSTEM: Indexer ---
            // We just call the "verb" methods
            if (gamepad2.rightBumperWasPressed()) {
                if (indexer.currentState == -7 || indexer.currentState == -5 || indexer.currentState == -3 || indexer.currentState == -1 || indexer.currentState == 1 || indexer.currentState == 3 || indexer.currentState == 5 || indexer.currentState == 7) {
                    indexer.advancecollecttoshoot();
                }
                if (indexer.currentState == -8 || indexer.currentState == -6 || indexer.currentState == -4 || indexer.currentState == -2 || indexer.currentState == 0 || indexer.currentState == 2 || indexer.currentState == 4 || indexer.currentState == 6 || indexer.currentState == 8) {
                    indexer.CtoC_or_StoS_POS();
                }
            }
            if (gamepad2.yWasPressed()) {
                if (indexer.currentState == -8 || indexer.currentState == -6 || indexer.currentState == -4 || indexer.currentState == -2 || indexer.currentState == 0 || indexer.currentState == 2 || indexer.currentState == 4 || indexer.currentState == 6 || indexer.currentState == 8) {
                    indexer.advancecollecttoshoot();
                }
                if (indexer.currentState == -7 || indexer.currentState == -5 || indexer.currentState == -3 || indexer.currentState == -1 || indexer.currentState == 1 || indexer.currentState == 3 || indexer.currentState == 5 || indexer.currentState == 7) {
                    indexer.CtoC_or_StoS_POS();
                }
            }
            if (gamepad2.leftBumperWasPressed()) {
                if (indexer.currentState == -7 || indexer.currentState == -5 || indexer.currentState == -3 || indexer.currentState == -1 || indexer.currentState == 1 || indexer.currentState == 3 || indexer.currentState == 5 || indexer.currentState == 7) {
                    indexer.deadvanceollecttoshoot();
                }
                if (indexer.currentState == -8 || indexer.currentState == -6 || indexer.currentState == -4 || indexer.currentState == -2 || indexer.currentState == 0 || indexer.currentState == 2 || indexer.currentState == 4 || indexer.currentState == 6 || indexer.currentState == 8) {
                    indexer.CtoC_or_StoS_Neg();
                }

            }
            if (gamepad2.bWasPressed()) {
                if (indexer.currentState == -8 || indexer.currentState == -6 || indexer.currentState == -4 || indexer.currentState == -2 || indexer.currentState == 0 || indexer.currentState == 2 || indexer.currentState == 4 || indexer.currentState == 6 || indexer.currentState == 8) {
                    indexer.deadvanceollecttoshoot();
                }
                if (indexer.currentState == -7 || indexer.currentState == -5 || indexer.currentState == -3 || indexer.currentState == -1 || indexer.currentState == 1 || indexer.currentState == 3 || indexer.currentState == 5 || indexer.currentState == 7) {
                    indexer.CtoC_or_StoS_Neg();
                }
                if (gamepad2.dpad_right) {
                    indexer.goToHome(); // Manual override
                }
            }
            // CRITICAL: The Indexer's state machine MUST be updated every loop
            indexer.update();


            // --- SUBSYSTEM: Transfer ---
            // This logic now "asks" the indexer if it's safe to fire
            if (gamepad2.x) {
                // Check the indexer's state before firing!
                if (indexer.isAtShootPosition()) {
//                               if (5 > indexer.getIndexerError() && indexer.getIndexerError() < -5) {
                    if (Math.abs(indexer.getIndexerError()) <= 3) {


                        transfer.fire();
                    }
                }
            } else {
                transfer.home();
            }

            // --- Telemetry ---
            telemetry.addData("--- Indexer ---", "");
            telemetry.addData("Indexer State", indexer.getState());
            telemetry.addData("Indexer Target", "%.1f", indexer.getTargetPosition());
            telemetry.addData("Indexer Actual", "%.1f", indexer.getCurrentPosition());
            telemetry.addData("--- Shooter ---", "");
            telemetry.addData("Shooter State (onOff)", onOff);
            telemetry.addData("--- Transfer ---", "");
            telemetry.addData("Ready to Fire?", indexer.isAtShootPosition());
            telemetry.update();
        }
    }
}





