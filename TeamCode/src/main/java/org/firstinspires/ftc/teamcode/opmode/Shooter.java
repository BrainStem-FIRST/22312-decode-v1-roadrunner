package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Shooter {

    // Hardware
    public DcMotorEx shooterMotor;
    public double error;

    // Internal State
    private boolean isShooting = false;

    // Target State (Defaults to Far Shot)
    private double currentTargetRPM = RobotConstants.SHOOTER_RPM_FAR;

    public Shooter(HardwareMap hwMap) {
        init(hwMap);
    }

    public void init(HardwareMap hwMap) {
        shooterMotor = hwMap.get(DcMotorEx.class, "shooterMotor");

        shooterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // --- PIDF TUNING IMPLEMENTATION ---
        // Load the coefficients from RobotConstants
        PIDFCoefficients tunedPIDF = new PIDFCoefficients(
                RobotConstants.SHOOTER_KP,
                RobotConstants.SHOOTER_KI,
                RobotConstants.SHOOTER_KD,
                RobotConstants.SHOOTER_KF
        );

        // Apply to motor
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, tunedPIDF);

        stop();
    }

    /**
     * Updates the target RPM dynamically.
     */
    public void setTargetRPM(double rpm) {
        this.currentTargetRPM = rpm;

        // If we are currently running, update the velocity immediately
        if (isShooting) {
            updateMotorVelocity();
        }
    }

    public void toggle() {
        isShooting = !isShooting;

        if (isShooting) {
            updateMotorVelocity();
        } else {
            shooterMotor.setVelocity(0);
            shooterMotor.setPower(0);
        }
    }

    private void updateMotorVelocity() {
        double targetTicksPerSec = currentTargetRPM * (RobotConstants.SHOOTER_TICKS_PER_REV / 60.0);
        shooterMotor.setVelocity(targetTicksPerSec);
    }

    public void stop() {
        isShooting = false;
        shooterMotor.setPower(0);
        shooterMotor.setVelocity(0);
    }

    public boolean isShooting() {
        return isShooting;
    }

    // Helper to see which mode we are in
    public double getTargetRPM() {
        return currentTargetRPM;
    }

    public boolean isReadyToFire() {
        if (!isShooting) return false;

        double currentRPM = getCurrentRPM();
        error = Math.abs(currentTargetRPM - currentRPM);
        double tolerance = currentTargetRPM * RobotConstants.SHOOTER_TOLERANCE_PERCENT;
        return error < tolerance;

    }

    public double getCurrentRPM() {
        return (shooterMotor.getVelocity() * 60.0) / RobotConstants.SHOOTER_TICKS_PER_REV;
    }
}