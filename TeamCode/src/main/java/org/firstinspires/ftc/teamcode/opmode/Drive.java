package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * The Drive subsystem manages all robot movement.
 * It contains the 4 drive motors and the logic for Mecanum/Omni drive.
 */
public class Drive {

    // Hardware
    private DcMotorEx frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;

    /**
     * Constructor for the Drive subsystem.
     * @param hwMap The hardware map from the OpMode.
     */
    public Drive(HardwareMap hwMap) {
        init(hwMap);
    }

    /**
     * Initializes all drive hardware components.
     * @param hwMap The hardware map from the OpMode.
     */
    public void init(HardwareMap hwMap) {
        // Hardware mapping
        frontLeftMotor = hwMap.get(DcMotorEx.class, "leftFrontDrive");
        frontRightMotor = hwMap.get(DcMotorEx.class, "rightFrontDrive");
        backLeftMotor = hwMap.get(DcMotorEx.class, "backLeftMotor");
        backRightMotor = hwMap.get(DcMotorEx.class, "backRightMotor");

        // Set motor modes
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set zero power behavior
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set motor directions (right side is typically reversed)
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /**
     * The main public method for driving the robot.
     * @param sideToSidePower  Gamepad Left Stick X (strafe)
     * @param forwardBackPower    Gamepad Left Stick Y (forward/back)
     * @param turnPower  Gamepad Right Stick X (turn)
     */
    public void drive(double sideToSidePower, double forwardBackPower, double turnPower) {
        // This logic is copied from the original setDrivePowers helper method.
        double addValue = Math.round((100 * (forwardBackPower * Math.abs(forwardBackPower) + sideToSidePower * Math.abs(sideToSidePower)))) / 100.;
        double subtractValue = Math.round((100 * (forwardBackPower * Math.abs(forwardBackPower) - sideToSidePower * Math.abs(sideToSidePower)))) / 100.;

        // Apply powers using the private helper method
        setMotorPowers(
                (addValue - turnPower),
                (subtractValue + turnPower),
                (subtractValue - turnPower),
                (addValue + turnPower)
        );
    }

    public void setMotorPowers(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower) {
        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }

}

