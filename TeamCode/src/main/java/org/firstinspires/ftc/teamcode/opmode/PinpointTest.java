package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Pinpoint Test")
public class PinpointTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // initialize pinpoint
        PinpointLocalizer pinpoint = new PinpointLocalizer(hardwareMap, new Pose2d(0, 0, 0), telemetry);

        waitForStart();

        while (opModeIsActive()) {
            // update pinpoint
            pinpoint.update();

            // print out pinpoint pose
            telemetry.addData("pinpoint x", pinpoint.pose().position.x);
            telemetry.addData("pinpoint y", pinpoint.pose().position.y);
            telemetry.addData("pinpoint heading", Math.toDegrees(pinpoint.pose().heading.toDouble()));

            telemetry.update();
        }
    }
}
