package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * The Intake subsystem manages the collector motor for intaking and outtaking game pieces.
 */
public class Intake {

    // Hardware
    private DcMotorEx collectorMotor;

    /**
     * Constructor for the Intake subsystem.
     * @param hwMap The hardware map from the OpMode.
     */
    public Intake(HardwareMap hwMap) {
        init(hwMap);
    }

    /**
     * Initializes all Intake hardware components.
     * @param hwMap The hardware map from the OpMode.
     */
    public void init(HardwareMap hwMap) {
        // Hardware mapping
       collectorMotor = hwMap.get(DcMotorEx.class, "collectorMotor");
        // Set motor mode
        collectorMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Spins the intake motor forward to collect game pieces.
     */
    public void runIntake() {
        collectorMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        collectorMotor.setPower(RobotConstants.INTAKE_POWER);
    }

    /**
     * Spins the intake motor in reverse to clear jams or outtake.
     */
    public void runOuttake() {
        collectorMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        collectorMotor.setPower(RobotConstants.OUTTAKE_POWER);
    }

    /**
     * Stops the intake motor.
     */
    public void stop() {
        collectorMotor.setPower(0);
    }
}
