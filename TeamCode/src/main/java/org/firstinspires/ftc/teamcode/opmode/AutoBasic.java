package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.commands.SpinIndexer;
import org.firstinspires.ftc.teamcode.opmode.Drive;
import org.firstinspires.ftc.teamcode.opmode.Intake;
import org.firstinspires.ftc.teamcode.opmode.Shooter;
import org.firstinspires.ftc.teamcode.opmode.Indexer;
import org.firstinspires.ftc.teamcode.opmode.Transfer;
import org.firstinspires.ftc.teamcode.opmode.Sensors;
import org.firstinspires.ftc.teamcode.robot.BrainSTEMRobot;
import org.firstinspires.ftc.teamcode.utils.PIDController;
import org.firstinspires.ftc.teamcode.utils.pidDrive.DriveParams;
import org.firstinspires.ftc.teamcode.utils.pidDrive.DrivePath;
import org.firstinspires.ftc.teamcode.utils.pidDrive.Tolerance;
import org.firstinspires.ftc.teamcode.utils.pidDrive.Waypoint;

@Autonomous(name = "Basic Autonomous")
public class AutoBasic extends LinearOpMode {
ServoImplEx lifter;

    Drive drive;
    Shooter shooter;
    Transfer transfer;
    PinpointLocalizer pinpoint;
    DcMotorEx indexerMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new Drive(hardwareMap);
        pinpoint = new PinpointLocalizer(hardwareMap, new Pose2d(0, 0, 0), telemetry);
        shooter = new Shooter(hardwareMap);
        transfer = new Transfer(hardwareMap);

        indexerMotor = hardwareMap.get(DcMotorEx.class, "indexerMotor");
        telemetry.addData(String.valueOf(indexerMotor), "indexerMotor");

        indexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        indexerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        indexerMotor.setPower(0);

        telemetry.addLine("Initialization Complete. Ready to start.");
        telemetry.update();




        waitForStart();

        transfer.lifter.setPwmRange(new PwmControl.PwmRange(

                RobotConstants.TRANSFER_LIFTER_DOWN_POS,   // This will be position 0.0
                RobotConstants.TRANSFER_LIFTER_RAISED_POS
        ));

        boolean finishedindexing = false;
        boolean finishedindexing_2 = false;
        boolean finishedindexing_3 = false;
        boolean reachedDestination = false;
        double targetX = -57.5;
        PIDController xPid = new PIDController(0.1, 0, 0);
        xPid.setTarget(targetX);

        while (reachedDestination == false) {

            pinpoint.update();

            double xPower = xPid.update(pinpoint.pose().position.x);
            if (xPower < -0.6) {
                xPower = -0.6;
            }
            if (xPower > 0.6) {
                xPower = 0.6;
            }
            drive.drive(0, xPower, 0);

            double xError = Math.abs(targetX - pinpoint.pose().position.x);

            if (xError < 2) {
                reachedDestination = true;
                drive.drive(0, 0, 0);
            }

            telemetry.addData("x", pinpoint.pose().position.x);
            telemetry.addData("y", pinpoint.pose().position.y);
            telemetry.addData("xError", Math.abs(targetX - pinpoint.pose().position.x));
            telemetry.update();
        }
        shooter.shooterMotor.setPower(0.9);
sleep(2200);
        while (false == finishedindexing) {

            double error = 49 - indexerMotor.getCurrentPosition();
            double power = 0.05 * error;
            power = Range.clip(power, -0.3, 0.3);
            indexerMotor.setPower(power);
            if (power < 0.1){
                if (power > 0){
                    power = 0.1;
                }

            }
            if (Math.abs(error) <= 1) {
                finishedindexing = true;
                indexerMotor.setPower(0);





            }
            telemetry.addData("indexer motor position", indexerMotor.getCurrentPosition());
            telemetry.update();

        }
        shooter.shooterMotor.setPower(0.88);
        sleep(1500);
        transfer.lifter.setPosition(0.99);
        sleep(1500);
        transfer.lifter.setPosition(0);
        sleep(1000);




        while (false == finishedindexing_2) {
            double error = 145 - indexerMotor.getCurrentPosition();
            double power = 0.07 * error;
            power = Range.clip(power, -0.3, 0.3);
            indexerMotor.setPower(power);
            if (power < 0.1){
                if (power > 0){
                    power = 0.1;
                }
                if (Math.abs(error) <= 1) {
                    finishedindexing_2 = true;
                    indexerMotor.setPower(0);

                }

            }

            telemetry.addData("indexer motor position2", indexerMotor.getCurrentPosition());
            telemetry.addData("is it done indexing the 2nd one?", finishedindexing_2);
            telemetry.update();

        }

        sleep(1000);
        transfer.lifter.setPosition(0.99);
        sleep(1500);
        transfer.lifter.setPosition(0);
        sleep(1000);

        while (false == finishedindexing_3) {

            double error = 238 - indexerMotor.getCurrentPosition();
            double power = 0.07 * error;
            power = Range.clip(power, -0.3, 0.3);
            indexerMotor.setPower(power);
            if (power < 0.1) {
                if (power > 0) {
                    power = 0.1;
                }

            }
            if (Math.abs(error) <= 1) {
                finishedindexing_3 = true;
                indexerMotor.setPower(0);

            }
        }
            sleep(1000);
            transfer.lifter.setPosition(0.99);
            sleep(1500);
            transfer.lifter.setPosition(0);
            sleep(1000);



            telemetry.addData("indexer motor position3", indexerMotor.getCurrentPosition());
            telemetry.update();

    }
}