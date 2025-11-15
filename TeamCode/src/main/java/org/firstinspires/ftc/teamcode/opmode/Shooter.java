package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * The Shooter subsystem manages the main shooter motor.
 */
public class Shooter {

    // Hardware
    public DcMotorEx shooterMotor;

    /**
     * Constructor for the Shooter subsystem.
     * @param hwMap The hardware map from the OpMode.
     */
    public Shooter(HardwareMap hwMap) {
        init(hwMap);
    }

    /**
     * Initializes all Shooter hardware components.
     * @param hwMap The hardware map from the OpMode.
     */
    public void init(HardwareMap hwMap) {
        // Hardware mapping
        shooterMotor = hwMap.get(DcMotorEx.class, "shooterMotor");
        // Set motor mode
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Runs the shooter motor at its constant power.
     */
    public void run() {
        shooterMotor.setPower(RobotConstants.SHOOTER_POWER);
    }

    /**
     * Stops the shooter motor.
     */
    public void stop() {
        shooterMotor.setPower(0);
    }
}
