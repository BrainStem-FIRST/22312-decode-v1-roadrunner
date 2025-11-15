package org.firstinspires.ftc.teamcode.commands;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.opmode.Indexer;
import org.firstinspires.ftc.teamcode.utils.CachingMotor;

import java.util.Collections;
import java.util.Set;


public class SpinIndexer implements Command {

    private final DcMotorEx motor;

    public SpinIndexer(DcMotorEx motor) {
        this.motor = motor;
    }


    @Override
    public void initialize() {
        motor.setTargetPosition(49);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(motor.getCurrentPosition() - motor.getTargetPosition()) <= 5;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Collections.emptySet();
    }
}
