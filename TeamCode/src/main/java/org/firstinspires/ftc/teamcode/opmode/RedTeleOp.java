package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Red TeleOp", group = "Competition")
public class RedTeleOp extends BrainSTEMTeleOp {

    @Override
    public double getVisionOffset() {
        return RobotConstants.VISION_OFFSET_RED;
    }
}