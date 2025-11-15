package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

/**
 * A central repository for all constants (like motor power, servo positions, and encoder ticks).
 * This makes tuning the robot much easier, as all "magic numbers" are in one place.
 */
public class RobotConstants {

    // --- Intake ---
    public static double INTAKE_TRIGGER_THRESHOLD = 0.001;
    public static double INTAKE_POWER = 0.99;
    public static double OUTTAKE_POWER = 0.99;

    // --- Shooter ---
    public static double SHOOTER_POWER = 0.95;

    // --- Transfer ---
    // These are the physical PWM pulse widths for the servo's min/max range.
    public static int TRANSFER_LIFTER_DOWN_POS = 1715; // "Home" position
    public static int TRANSFER_LIFTER_RAISED_POS = 1350; // "Fire" position


    // --- Indexer ---
    public static double INDEXER_POWER = 0.5;
    public static double INDEXER_HOME_POS = 15;
    public static double INDEXER_CYCLE_POSITIONS = 288.175; // Encoder ticks for one full 3-piece cycle

    // --- Indexer Forward States (Encoder Ticks) ---
    public static double INDEXER_STATE_1_POS = 20;   // Intake 1
    public static double INDEXER_STATE_2_POS = 49;   // Shoot 1
    public static double INDEXER_STATE_3_POS = 110;  // Intake 2
    public static double INDEXER_STATE_4_POS = 145;  // Shoot 2
    public static double INDEXER_STATE_5_POS = 210;  // Intake 3
    public static double INDEXER_STATE_6_POS = 238;  // Shoot 3



    // --- Indexer Reverse State Offsets (Encoder Ticks) ---
    // These are the "magic numbers" from the original file, now named.
    public static double INDEXER_STATE_NEG_1_OFFSET = -40;  // (collectPos1 - 40)
    public static double INDEXER_STATE_NEG_2_OFFSET = -98;  // (shootPos1 - 98)
    public static double INDEXER_STATE_NEG_3_OFFSET = -220; // (collectPos2 - 220)
    public static double INDEXER_STATE_NEG_4_OFFSET = -290; // (shootPos2 - 290)
    public static double INDEXER_STATE_NEG_5_OFFSET = -420; // (collectPos3 - 420)
    public static double INDEXER_STATE_NEG_6_OFFSET = -476; // (shootPos3 - 476)


}
