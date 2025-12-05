package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Config
public class Indexer {
    public static double flickTime = 0.2;
    public ElapsedTime flickTimer = new ElapsedTime();

    public DcMotorEx indexerMotor;

    // State (Public per request)
    public int currentState = 0;
    public double currentTargetPosition = 0;

    // Looping position variables
    private double collectPos1, collectPos2, collectPos3;
    private double shootPos1, shootPos2, shootPos3;

    // Tuning variable
    public static double kP = 0.007;

    public Indexer(HardwareMap hwMap) {
        init(hwMap);
    }

    public void init(HardwareMap hwMap) {
        indexerMotor = hwMap.get(DcMotorEx.class, "indexerMotor");

        indexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        indexerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        indexerMotor.setPower(0);

        resetPositionVariables();
        flickTimer.reset();
    }

    /**
     * Logic for the Right Bumper.
     * Original Behavior:
     * - If in Collect (Odd): Move to Shoot (CtoS) [+1]
     * - If in Shoot (Even): Move to next Shoot (StoS) [+2]
     */
    public void handleRapidFire() {
        if (isOddState()) {
            CtoS_or_StoC_Advance(); // CtoS (+1)
        } else {
            CtoC_or_StoS_Advance(); // StoS (+2)
        }
    }
    public void handleRightBumper() {
        if (flickTimer.seconds() > flickTime) {
            if (isOddState()) {
                CtoS_or_StoC_Advance(); // CtoS (+1)
            } else {
                CtoC_or_StoS_Advance(); // StoS (+2)
            }
        }
    }

    /**
     * Logic for the Y Button.
     * Original Behavior:
     * - If in Collect (Odd): Move to next Collect (CtoC) [+2]
     * - If in Shoot (Even): Move to Collect (StoC) [+1]
     */
    public void handleYButton() {
        if (isOddState()) {
            CtoC_or_StoS_Advance(); // CtoC (+2)
        } else {
            CtoS_or_StoC_Advance(); // StoC (+1)
        }
    }

    /**
     * Logic for Left Bumper (Reverse RB logic).
     */
    public void handleLeftBumper() {
        if (isOddState()) {
            CtoS_or_StoC_Reverse();
        } else {
            CtoC_or_StoS_Reverse();
        }
    }

    /**
     * Logic for B Button (Reverse Y logic).
     */
    public void handleBButton() {
        if (isOddState()) {
            CtoC_or_StoS_Reverse();
        } else {
            CtoS_or_StoC_Reverse();
        }
    }

    // --- Internal Nomenclature Methods ---

    // CtoS (Collect to Shoot) or StoC (Shoot to Collect) -> Moves 1 Step
    public void CtoS_or_StoC_Advance() {
        currentState = currentState + 1;
        if (currentState > 6) {
            currentState = 1;
            incrementAllPositions(RobotConstants.INDEXER_TICKS_PER_CYCLE);
        }
    }

    public void CtoS_or_StoC_Reverse() {
        currentState = currentState - 1;
        if (currentState < -6) {
            currentState = -1;
            incrementAllPositions(-RobotConstants.INDEXER_TICKS_PER_CYCLE);
        }
    }

    // CtoC (Collect to Collect) or StoS (Shoot to Shoot) -> Moves 2 Steps
    public void CtoC_or_StoS_Advance() {
        currentState = currentState + 2;
        if (currentState > 6) {
            // Logic: If we were even, reset to 2. If odd, reset to 1.
            if (isEvenState(currentState)) currentState = 2;
            else currentState = 1;

            incrementAllPositions(RobotConstants.INDEXER_TICKS_PER_CYCLE);
        }
    }

    public void CtoC_or_StoS_Reverse() {
        currentState = currentState - 2;
        if (currentState < -6) {
            if (isEvenState(currentState)) currentState = -2;
            else currentState = -1;

            incrementAllPositions(-RobotConstants.INDEXER_TICKS_PER_CYCLE);
        }
    }

    public void incrementAllPositions(double amount) {
        collectPos1 += amount;
        collectPos2 += amount;
        collectPos3 += amount;
        shootPos1 += amount;
        shootPos2 += amount;
        shootPos3 += amount;
    }

    public void update() {
        // Map State to Target
        if (currentState == 1) currentTargetPosition = collectPos1;
        else if (currentState == 2) currentTargetPosition = shootPos1;
        else if (currentState == 3) currentTargetPosition = collectPos2;
        else if (currentState == 4) currentTargetPosition = shootPos2;
        else if (currentState == 5) currentTargetPosition = collectPos3;
        else if (currentState == 6) currentTargetPosition = shootPos3;

            // Negative / Backup States
        else if (currentState == -1) currentTargetPosition = collectPos1 + RobotConstants.INDEXER_BACKUP_OFFSET_1;
        else if (currentState == -2) currentTargetPosition = shootPos1 + RobotConstants.INDEXER_BACKUP_OFFSET_2;
        else if (currentState == -3) currentTargetPosition = collectPos2 + RobotConstants.INDEXER_BACKUP_OFFSET_3;
        else if (currentState == -4) currentTargetPosition = shootPos2 + RobotConstants.INDEXER_BACKUP_OFFSET_4;
        else if (currentState == -5) currentTargetPosition = collectPos3 + RobotConstants.INDEXER_BACKUP_OFFSET_5;
        else if (currentState == -6) currentTargetPosition = shootPos3 + RobotConstants.INDEXER_BACKUP_OFFSET_6;

            // Loop Transitions
        else if (currentState == -7) currentState = -1;
        else if (currentState == -8) currentState = -2;
        else if (currentState == 7) currentState = 1;
        else if (currentState == 8) currentState = 2;

        // Motor Power
        if (currentState != 0) {
            double error = currentTargetPosition - indexerMotor.getCurrentPosition();
            double power = kP * error;
            power = Range.clip(power, -1, 1);
            indexerMotor.setPower(power);
        }
    }

    public void goToHome() {
        currentState = 0;
        resetPositionVariables();
        indexerMotor.setTargetPosition((int) RobotConstants.INDEXER_HOME_POS);
        indexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        indexerMotor.setPower(RobotConstants.INDEXER_POWER);
    }
    public void IndexerAutoToTele() {
        indexerMotor.setTargetPosition((0));
        indexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        indexerMotor.setPower(RobotConstants.INDEXER_POWER);
    }

    // Helpers
    private boolean isOddState() {
        // -7, -5... 1, 3, 5, 7
        return Math.abs(currentState) % 2 == 1;
    }

    private boolean isEvenState(int state) {
        return Math.abs(state) % 2 == 0;
    }

    public boolean isAtShootPosition() {
        return currentState == 0 || isEvenState(currentState);
    }

    public double getIndexerError() {
        return indexerMotor.getCurrentPosition() - currentTargetPosition;
    }

    private void resetPositionVariables() {
        collectPos1 = RobotConstants.INDEXER_STATE_1_POS;
        collectPos2 = RobotConstants.INDEXER_STATE_3_POS;
        collectPos3 = RobotConstants.INDEXER_STATE_5_POS;
        shootPos1 = RobotConstants.INDEXER_STATE_2_POS;
        shootPos2 = RobotConstants.INDEXER_STATE_4_POS;
        shootPos3 = RobotConstants.INDEXER_STATE_6_POS;
    }
}