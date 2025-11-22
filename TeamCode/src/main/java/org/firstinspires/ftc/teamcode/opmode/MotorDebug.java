package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "DEBUG: Motor Check")
public class MotorDebug extends LinearOpMode {

    private DcMotorEx fl, fr, bl, br;

    @Override
    public void runOpMode() {
        // Map to the Config Names
        fl = hardwareMap.get(DcMotorEx.class, "leftFrontDrive");
        fr = hardwareMap.get(DcMotorEx.class, "rightFrontDrive");
        bl = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        br = hardwareMap.get(DcMotorEx.class, "backRightMotor");

        telemetry.addLine("Press X / Y / A / B to spin individual motors.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.x) {
                fl.setPower(0.3);
                telemetry.addData("Running", "Front Left (X)");
            } else {
                fl.setPower(0);
            }

            if (gamepad1.y) {
                fr.setPower(0.3);
                telemetry.addData("Running", "Front Right (Y)");
            } else {
                fr.setPower(0);
            }

            if (gamepad1.a) {
                bl.setPower(0.3);
                telemetry.addData("Running", "Back Left (A)");
            } else {
                bl.setPower(0);
            }

            if (gamepad1.b) {
                br.setPower(0.3);
                telemetry.addData("Running", "Back Right (B)");
            } else {
                br.setPower(0);
            }

            telemetry.update();
        }
    }
}