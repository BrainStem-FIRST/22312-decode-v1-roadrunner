package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * The Drive subsystem manages all robot movement.
 * Fixed: Turn logic inverted to match driver inputs.
 */
public class Drive {

    // Hardware Objects
    private DcMotorEx frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;

    public Drive(HardwareMap hwMap) {
        init(hwMap);
    }

    public void init(HardwareMap hwMap) {
        // --- HARDWARE MAPPING ---
        // Names must match the Robot Controller configuration EXACTLY.
        frontLeftMotor = hwMap.get(DcMotorEx.class, "frontLeftMotor");
        frontRightMotor = hwMap.get(DcMotorEx.class, "frontRightMotor");
        backLeftMotor = hwMap.get(DcMotorEx.class, "backLeftMotor");
        backRightMotor = hwMap.get(DcMotorEx.class, "backRightMotor");

        // --- MOTOR MODES ---
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // --- ZERO POWER BEHAVIOR ---
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // --- MOTOR DIRECTIONS ---
        // Left Side = FORWARD
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        // Right Side = REVERSE (Standard Mecanum setup)
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /**
     * Standard Mecanum Drive Calculation.
     * @param strafe      Left Stick X
     * @param forward     Left Stick Y
     * @param turn        Right Stick X
     */
    public void drive(double strafe, double forward, double turn) {

        // Denominator ensures we don't exceed motor limits
        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(turn), 1);

        // KINEMATICS FIX: Inverted the 'turn' signs here.
        // Previous: (forward + strafe + turn)
        // New:      (forward + strafe - turn)

        double frontLeftPower  = (forward + strafe - turn) / denominator;
        double backLeftPower   = (forward - strafe - turn) / denominator;
        double frontRightPower = (forward - strafe + turn) / denominator;
        double backRightPower  = (forward + strafe + turn) / denominator;

        setMotorPowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }

    public void setMotorPowers(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower) {
        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }
}