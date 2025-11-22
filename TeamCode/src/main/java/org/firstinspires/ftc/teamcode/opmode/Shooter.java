package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * The Shooter subsystem manages the main shooter motor.
 */
public class Shooter {

    // Hardware
    public DcMotorEx shooterMotor;

    // Internal State for Toggle
    private boolean isShooting = false;

    public Shooter(HardwareMap hwMap) {
        init(hwMap);
    }

    public void init(HardwareMap hwMap) {
        shooterMotor = hwMap.get(DcMotorEx.class, "shooterMotor");
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // FIX: Reversed motor direction based on user feedback
        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        stop();
    }

    public void toggle() {
        isShooting = !isShooting;

        if (isShooting) {
            shooterMotor.setPower(RobotConstants.SHOOTER_POWER);
        } else {
            shooterMotor.setPower(0);
        }
    }

    public void stop() {
        isShooting = false;
        shooterMotor.setPower(0);
    }

    public boolean isShooting() {
        return isShooting;
    }
}