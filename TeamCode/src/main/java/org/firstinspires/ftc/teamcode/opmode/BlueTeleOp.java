package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Blue TeleOp", group = "Competition")
public class BlueTeleOp extends BrainSTEMTeleOp {

    @Override
    public double getVisionOffset() {
        return RobotConstants.VISION_OFFSET_BLUE;
    }
}