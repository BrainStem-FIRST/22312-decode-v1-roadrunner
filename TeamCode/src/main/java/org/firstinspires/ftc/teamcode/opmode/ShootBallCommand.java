package org.firstinspires.ftc.teamcode.opmode;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.Subsystem;

import java.util.Collections;
import java.util.Set;

public class ShootBallCommand implements Command {
    @Override
    public Set<Subsystem> getRequirements() {
        return Collections.emptySet();
    }

    @Override
    public void execute() {
//        shooterMotor.setPower(1);
//        flipper.setPosition(Flipper.upposition);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
