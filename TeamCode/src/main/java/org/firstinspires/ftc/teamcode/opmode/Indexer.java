package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * The Indexer subsystem manages the state machine for holding, rotating,
 * and lining up game pieces for the Transfer/Shooter.
 */
public class Indexer {

    // Hardware
    public DcMotorEx indexerMotor;

    // State
    public int currentState = 0; // Replaces 'NumberoftimesYpressed'
    public double currentTargetPosition = 0;

    // These variables store the "looping" positions. They get added to on each cycle.
    private double collectPos1, collectPos2, collectPos3;
    private double shootPos1, shootPos2, shootPos3;

    private double kP = 0.005;
    /**
     * Constructor for the Indexer subsystem.
     * @param hwMap The hardware map from the OpMode.
     */
    public Indexer(HardwareMap hwMap) {
        init(hwMap);
    }

    /**
     * Initializes all Indexer hardware components and state.
     * @param hwMap The hardware map from the OpMode.
     */
    public void init(HardwareMap hwMap) {
        // Hardware mapping
        indexerMotor = hwMap.get(DcMotorEx.class, "indexerMotor");

        // CRITICAL: Reset encoders to 0 on init
        indexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        indexerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        indexerMotor.setPower(0);

        // Initialize our position variables from constants
        resetPositionVariables();
    }

    /**
     * This is the main "loop" method for the Indexer.
     * It MUST be called every single loop in the TeleOp.
     * It reads the internal state and tells the motor where to go.
     */
    public void update() {
        // This 'if/else if' block replaces the giant list in the original TeleOp.
        if (currentState == 1) {
            currentTargetPosition = collectPos1;
        } else if (currentState == 2) {
            currentTargetPosition = shootPos1;
        } else if (currentState == 3) {
            currentTargetPosition = collectPos2;
        } else if (currentState == 4) {
            currentTargetPosition = shootPos2;
        } else if (currentState == 5) {
            currentTargetPosition = collectPos3;
        } else if (currentState == 6) {
            currentTargetPosition = shootPos3;
        } else if (currentState == -1) {
            currentTargetPosition = collectPos1 + RobotConstants.INDEXER_STATE_NEG_1_OFFSET;
        } else if (currentState == -2) {
            currentTargetPosition = shootPos1 + RobotConstants.INDEXER_STATE_NEG_2_OFFSET;
        } else if (currentState == -3) {
            currentTargetPosition = collectPos2 + RobotConstants.INDEXER_STATE_NEG_3_OFFSET;
        } else if (currentState == -4) {
            currentTargetPosition = shootPos2 + RobotConstants.INDEXER_STATE_NEG_4_OFFSET;
        } else if (currentState == -5) {
            currentTargetPosition = collectPos3 + RobotConstants.INDEXER_STATE_NEG_5_OFFSET;
        } else if (currentState == -6) {
            currentTargetPosition = shootPos3 + RobotConstants.INDEXER_STATE_NEG_6_OFFSET;
        } else if (currentState == -7) {
            currentState = -1;
        } else if (currentState == -8) {
            currentState = -2;
        } else if (currentState == 7) {
            currentState = 1;
        } else if (currentState == 8) {
            currentState = 2;
        }

        // Only command the motor if we are in an active state (not 0)
        if (currentState != 0) {
//            indexerMotor.setTargetPosition((int) currentTargetPosition);
//            indexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            indexerMotor.setPower(RobotConstants.INDEXER_POWER);
            double error = currentTargetPosition - indexerMotor.getCurrentPosition();
            double power = kP * error;
            power = Range.clip(power, -1, 1);
            indexerMotor.setPower(power);
        }
    }

    /**
     * Advances the indexer state to the next position.
     */
    public void advancecollecttoshoot() {
        currentState = currentState + 1;
        if (currentState > 6) {
            currentState = 1; // Loop back to state 1
            // Add the cycle distance to all positions for the next loop
            collectPos1 =  collectPos1 + RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos2 = collectPos2 + RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos3 = collectPos3 + RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos1 = shootPos1 + RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos2 = shootPos2 + RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos3 = shootPos3 + RobotConstants.INDEXER_CYCLE_POSITIONS;
        }
    }
    public void deadvanceollecttoshoot() {
        currentState = currentState - 1;
        if (currentState < -6) {
            currentState = -1; // Loop back to state 1
            // Add the cycle distance to all positions for the next loop
            collectPos1 = collectPos1 - RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos2 = collectPos2 - RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos3 = collectPos3 - RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos1 = shootPos1 -RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos2 = shootPos2 - RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos3 = shootPos3 -RobotConstants.INDEXER_CYCLE_POSITIONS;
        }
    }
    public void CtoC_or_StoS_POS () {
        currentState = currentState + 2;
        if (currentState > 6) {
            if (currentState == -8 || currentState == -6 || currentState == -4 || currentState == -2 || currentState == 0 || currentState == 2 || currentState == 4 || currentState == 6) {
                currentState = 2;
            }
            if (currentState == -7 || currentState == -5 || currentState == -3 || currentState == -1 || currentState == 1 || currentState == 3 || currentState == 5 || currentState == 7) {
                currentState = 1;
            }

            // Add the cycle distance to all positions for the next loop
            collectPos1 += RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos2 += RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos3 += RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos1 += RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos2 += RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos3 += RobotConstants.INDEXER_CYCLE_POSITIONS;
        }
    }
    public void CtoC_or_StoS_Neg () {
        currentState = currentState - 2;
        if (currentState < -6) {
            if (currentState == -8 || currentState == -6 || currentState == -4 || currentState == -2 || currentState == 0 || currentState == 2 || currentState == 4 || currentState == 6) {
                currentState = -2;
            }
            if (currentState == -7 || currentState == -5 || currentState == -3 || currentState == -1 || currentState == 1 || currentState == 3 || currentState == 5 || currentState == 7) {
                currentState = -1;
            }

            // Add the cycle distance to all positions for the next loop
            collectPos1 -= RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos2 -= RobotConstants.INDEXER_CYCLE_POSITIONS;
            collectPos3 -= RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos1 -= RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos2 -= RobotConstants.INDEXER_CYCLE_POSITIONS;
            shootPos3 -= RobotConstants.INDEXER_CYCLE_POSITIONS;
        }
    }

    /**
     * Reverses the indexer state to the previous position.
     */
    public void reverse() {
        currentState--;
        if (currentState < -6) { // Original logic was '== -7'
            if (currentState == -7) {
                currentState = -1; // Loop back to state -1
            }
        }
        if (currentState < -6) {
            currentState = -2; // Loop back to state -2
        }
    }

    /**
     * Manual override to send the indexer to its home position.
     * This also resets the main state.
     */
    public void goToHome() {
        currentState = 0; // Set state to idle
        resetPositionVariables(); // Reset the cycle counts

        // Command the motor to the home position
        indexerMotor.setTargetPosition((int) RobotConstants.INDEXER_HOME_POS);
        indexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        indexerMotor.setPower(RobotConstants.INDEXER_POWER);
    }

    /**
     * Checks if the indexer is currently at a "shoot" position.
     * @return true if the indexer is ready to be fired.
     */
    public boolean isAtShootPosition() {
        // We also include 0 from the original file, which is our 'home' state.
        // This implies you can fire from the home position.
        // Let's match the original file:
        // (NumberoftimesYpressed == 2 || NumberoftimesYpressed == 4 || NumberoftimesYpressed == 6 || NumberoftimesYpressed == 0 || NumberoftimesYpressed == -2|| NumberoftimesYpressed == -4|| NumberoftimesYpressed == -6)
        return currentState == 0 || currentState == 2 || currentState == 4 || currentState == 6 ||
                currentState == -2 || currentState == -4 || currentState == -6;
    }

    /**
     * Resets all looping position variables back to their base constants.
     */
    private void resetPositionVariables() {
        collectPos1 = RobotConstants.INDEXER_STATE_1_POS;
        collectPos2 = RobotConstants.INDEXER_STATE_3_POS;
        collectPos3 = RobotConstants.INDEXER_STATE_5_POS;
        shootPos1 = RobotConstants.INDEXER_STATE_2_POS;
        shootPos2 = RobotConstants.INDEXER_STATE_4_POS;
        shootPos3 = RobotConstants.INDEXER_STATE_6_POS;
    }


    // --- Telemetry Methods ---
    public int getState() { return currentState; }
    public double getIndexerError() {
        return indexerMotor.getCurrentPosition() - currentTargetPosition;
    }
    public double getTargetPosition() { return currentTargetPosition; }
    public double getCurrentPosition() { return indexerMotor.getCurrentPosition(); }
}
