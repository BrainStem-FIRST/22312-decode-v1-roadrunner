package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
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
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
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
//        pinpoint = new PinpointLocalizer(hardwareMap, new Pose2d(0, 0, 0), telemetry);
//        shooter = new Shooter(hardwareMap);
//        transfer = new Transfer(hardwareMap);

//        indexerMotor = hardwareMap.get(DcMotorEx.class, "indexerMotor");
        telemetry.addData(String.valueOf(indexerMotor), "indexerMotor");

//        indexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        indexerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        indexerMotor.setPower(0);

//x is 34.8634 y is 61.6041 heading is 270//
        //x us 45.5 and y is 64.5 heading is 44
        // 1. Define the Initial Pose
        // Heading of 270 degrees means: +X is Forward, -Y is Right
        Pose2d initialPose = new Pose2d(-55.5, 44.5, Math.toRadians(135));
        Pose2d pose2 = new Pose2d(-24, 24, Math.toRadians(135));
        Pose2d pose3 = new Pose2d(-13.7, 22.4, Math.toRadians(90));
      //  Pose2d pose4 = new Pose2d(-12.3, 23.6, 90);
       // Pose2d pose3 = new Pose2d(36, -12, Math.toRadians(150));
        //Pose2d pose4 = new Pose2d(36,-12.1,Math.toRadians(102.5));
       // Pose2d
        //Pose2d pose4 = new Pose2d(36, -12, Math.toRadians(90));



        // --- NOTE: InitialPose2 is not needed and has been removed ---

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // --- PROBLEM: The old traj1, traj2, traj3 definitions are deleted ---
        // We now define ONE chained action instead:
            Action action1 = drive.actionBuilder(initialPose)

                // 1. FIRST MOVEMENT: Go Forward 48 inches
                // We add 48 inches to the starting X-coordinate (34.8634 + 48)
                .splineToLinearHeading(pose2, Math.toRadians(0))



                    //.splineToLinearHeading(pose4, Math.toRadians(0))
                .build();
        //Action action2 = drive.actionBuilder(pose3)
               // .splineToLinearHeading(pose4,Math.toRadians(0))

                        //.build();
        //Action action3 = drive.actionBuilder(pose5)
        Action action2 = drive.actionBuilder(pose2)
                .splineToLinearHeading(pose3, Math.toRadians(0))

                        .build();
        //Action action3 = drive.actionBuilder(pose3)
               // .splineToLinearHeading(pose4, Math.toRadians(0))

              //  .build();







        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        action1

                )
        );
        sleep(1000);
        Actions.runBlocking(
                new SequentialAction(
                        action2


                )
        );
        sleep(1000);
        //Actions.runBlocking(
                //new SequentialAction(
                //        action3



             //   )
      //  );

        // 4. EXECUTE THE SINGLE, FIXED ACTION




    }

}