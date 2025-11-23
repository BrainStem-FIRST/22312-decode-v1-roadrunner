package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

public class RobotConstants {

    // --- Intake ---
    public static double INTAKE_TRIGGER_THRESHOLD = 0.001;
    public static double INTAKE_POWER = 0.99;
    public static double OUTTAKE_POWER = 0.99;

    // --- Shooter Hardware ---
    public static double SHOOTER_TICKS_PER_REV = 28.0;
    public static double SHOOTER_MAX_RPM = 6000.0;
    public static double SHOOTER_TOLERANCE_PERCENT = 0.05; // 5%

    // --- Shooter Tuning (User Tuned) ---
    public static double SHOOTER_KF = 14.0;  // Feedforward
    public static double SHOOTER_KP = 50.0;  // Proportional
    public static double SHOOTER_KI = 0.0;   // Integral
    public static double SHOOTER_KD = 0.0;   // Derivative

    // --- Shooter Targets ---
    public static double SHOOTER_RPM_FAR = 4425.0;  // Default / Far Shot
    public static double SHOOTER_RPM_NEAR = 3600.0; // Near Shot

    // --- Transfer (Servo) ---
    // TUNED VALUES (Post-Rebuild)
    public static int TRANSFER_LIFTER_DOWN_POS = 2284; // Home / Intake Position
    public static int TRANSFER_LIFTER_RAISED_POS = 1587; // Fire / Shooter Position

    // --- Indexer (Encoder Ticks) ---
    public static double INDEXER_POWER = 0.5;
    public static double INDEXER_HOME_POS = 15;
    public static double INDEXER_TICKS_PER_CYCLE = 288.175;

    // Indexer Forward States
    public static double INDEXER_STATE_1_POS = 20;   // Intake 1
    public static double INDEXER_STATE_2_POS = 49;   // Shoot 1
    public static double INDEXER_STATE_3_POS = 110;  // Intake 2
    public static double INDEXER_STATE_4_POS = 145;  // Shoot 2
    public static double INDEXER_STATE_5_POS = 210;  // Intake 3
    public static double INDEXER_STATE_6_POS = 238;  // Shoot 3

    // Indexer Reverse Offsets
    public static double INDEXER_BACKUP_OFFSET_1 = -40;
    public static double INDEXER_BACKUP_OFFSET_2 = -98;
    public static double INDEXER_BACKUP_OFFSET_3 = -220;
    public static double INDEXER_BACKUP_OFFSET_4 = -290;
    public static double INDEXER_BACKUP_OFFSET_5 = -420;
    public static double INDEXER_BACKUP_OFFSET_6 = -476;

    // --- Vision ---
    public static double VISION_TURN_KP = 0.03;
    public static double VISION_MAX_TURN_SPEED = 0.5;
    public static double VISION_TOLERANCE_DEGREES = 1.0;
}