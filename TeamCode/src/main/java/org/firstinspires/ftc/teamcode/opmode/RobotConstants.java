package org.firstinspires.ftc.teamcode.opmode;

public class RobotConstants {

    // --- Intake ---
    public static double INTAKE_TRIGGER_THRESHOLD = 0.001;
    public static double INTAKE_POWER = 0.99;
    public static double OUTTAKE_POWER = 0.99;

    // --- Shooter ---
    public static double SHOOTER_TICKS_PER_REV = 28.0;
    public static double SHOOTER_MAX_RPM = 6000.0;
    public static double SHOOTER_TOLERANCE_PERCENT = 0.05;
    public static double SHOOTER_KF = 14.0;
    public static double SHOOTER_KP = 50.0;
    public static double SHOOTER_KI = 0.0;
    public static double SHOOTER_KD = 0.0;
    public static double SHOOTER_RPM_FAR = 4250.0;
    public static double SHOOTER_RPM_NEAR = 3300.0;

    // --- Transfer ---
    public static int TRANSFER_LIFTER_DOWN_POS = 2284;
    public static int TRANSFER_LIFTER_RAISED_POS = 1587;

    // --- AUTO FIRE TIMING ---
    public static long AF_INDEXER_SETTLE_MS = 130;
    public static long AF_TRANSFER_UP_MS = 130;
    public static long AF_TRANSFER_DOWN_MS = 130;

    // --- Vision & Aiming ---
    public static double VISION_TURN_KP = 0.03;
    public static double VISION_MAX_TURN_SPEED = 0.5;
    public static double VISION_TOLERANCE_DEGREES = 1.0;

    // GLOBAL OFFSET (Used by the code)
    public static double VISION_AIM_OFFSET = 0.0;

    // ALLIANCE SPECIFIC OFFSETS (Tune these!)
    // If Red shoots too far left, change this number.
    public static double VISION_OFFSET_RED = -2.0;
    // If Blue shoots too far right, change this number.
    public static double VISION_OFFSET_BLUE = 2.0;

    // --- Indexer ---
    public static double INDEXER_POWER = 0.5;
    public static double INDEXER_HOME_POS = 15;
    public static double INDEXER_TICKS_PER_CYCLE = 288.175;
    public static double INDEXER_STATE_1_POS = 20;
    public static double INDEXER_STATE_2_POS = 49;
    public static double INDEXER_STATE_3_POS = 110;
    public static double INDEXER_STATE_4_POS = 145;
    public static double INDEXER_STATE_5_POS = 210;
    public static double INDEXER_STATE_6_POS = 238;
    public static double INDEXER_BACKUP_OFFSET_1 = -40;
    public static double INDEXER_BACKUP_OFFSET_2 = -98;
    public static double INDEXER_BACKUP_OFFSET_3 = -220;
    public static double INDEXER_BACKUP_OFFSET_4 = -290;
    public static double INDEXER_BACKUP_OFFSET_5 = -420;
    public static double INDEXER_BACKUP_OFFSET_6 = -476;
}