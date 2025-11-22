package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

/**
 * A central repository for all constants.
 * REBUILD NOTE: Many of these values (marked TODO) must be retuned for the new mechanism.
 */
public class RobotConstants {

    // --- Intake ---
    public static double INTAKE_TRIGGER_THRESHOLD = 0.001; // TODO: Consider raising to 0.1 if too sensitive
    public static double INTAKE_POWER = 0.99;
    public static double OUTTAKE_POWER = 0.99;

    // --- Shooter ---
    public static double SHOOTER_POWER = 0.95;

    // --- Transfer (Servo) ---
    // TODO: TUNE THESE FIRST! If the servo arm was remounted, these will be wrong.
    // Start with "Safe" numbers if unsure.
    public static int TRANSFER_LIFTER_DOWN_POS = 2284; // "Home" position
    public static int TRANSFER_LIFTER_RAISED_POS = 1587; // "Fire" position

    // --- Indexer (Encoder Ticks) ---
    public static double INDEXER_POWER = 0.5;
    public static double INDEXER_HOME_POS = 15;

    // TODO: Recalculate this. Move the indexer 1 slot manually and check the encoder count.
    public static double INDEXER_TICKS_PER_CYCLE = 288.175;

    // --- Indexer Forward States (Encoder Ticks) ---
    public static double INDEXER_STATE_1_POS = 20;   // Intake 1
    public static double INDEXER_STATE_2_POS = 49;   // Shoot 1
    public static double INDEXER_STATE_3_POS = 110;  // Intake 2
    public static double INDEXER_STATE_4_POS = 145;  // Shoot 2
    public static double INDEXER_STATE_5_POS = 210;  // Intake 3
    public static double INDEXER_STATE_6_POS = 238;  // Shoot 3

    // --- Indexer Reverse Offsets ---
    // TODO: These move the indexer BACKWARDS to clear jams.
    // If the geometry changed, check that these don't hit the previous ball.
    // These correspond to (collectPos1 - 40), (shootPos1 - 98), etc.
    public static double INDEXER_BACKUP_OFFSET_1 = -40;
    public static double INDEXER_BACKUP_OFFSET_2 = -98;
    public static double INDEXER_BACKUP_OFFSET_3 = -220;
    public static double INDEXER_BACKUP_OFFSET_4 = -290;
    public static double INDEXER_BACKUP_OFFSET_5 = -420;
    public static double INDEXER_BACKUP_OFFSET_6 = -476;
}