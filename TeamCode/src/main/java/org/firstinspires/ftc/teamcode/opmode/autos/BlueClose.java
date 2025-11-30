package org.firstinspires.ftc.teamcode.opmode.autos;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.opmode.Drive;
import org.firstinspires.ftc.teamcode.opmode.Indexer;
import org.firstinspires.ftc.teamcode.opmode.Intake;
import org.firstinspires.ftc.teamcode.opmode.PinpointLocalizer;
import org.firstinspires.ftc.teamcode.opmode.RobotConstants;
import org.firstinspires.ftc.teamcode.opmode.Shooter;
import org.firstinspires.ftc.teamcode.opmode.Transfer;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Blue Close Auto")
public class BlueClose extends LinearOpMode {
    ServoImplEx lifter;
    Intake intake;
    Drive drive;

    Shooter shooter;
    Transfer transfer;
    Indexer indexer;
    PinpointLocalizer pinpoint;

    @Override
    public void runOpMode() throws InterruptedException {
        intake = new Intake(hardwareMap);
        drive = new Drive(hardwareMap);
//        pinpoint = new PinpointLocalizer(hardwareMap, new Pose2d(0, 0, 0), telemetry);
        shooter = new Shooter(hardwareMap);
        transfer = new Transfer(hardwareMap);
        indexer = new Indexer(hardwareMap);


//x is 34.8634 y is 61.6041 heading is 270//
        //x us 45.5 and y is 64.5 heading is 44
        // 1. Define the Initial Pose
        // Heading of 270 degrees means: +X is Forward, -Y is Right
        Pose2d initialPose = new Pose2d(-55.5, -44.5, Math.toRadians(-135));
        Pose2d pose2 = new Pose2d(-24, -24, Math.toRadians(-135));
        Pose2d pose3 = new Pose2d(-9.5, -22.4, Math.toRadians(-80));
        Pose2d pose4 = new Pose2d(-9.5, -33.5, Math.toRadians(-80));
        Pose2d pose5 = new Pose2d(-9.5, -37.5, Math.toRadians(-87.5));
        Pose2d pose6 = new Pose2d(-9.5, -43.5, Math.toRadians(-90));
        Pose2d pose7 = new Pose2d(-24.1, -24, Math.toRadians(-142));

      //  Pose2d pose4 = new Pose2d(-12.3, 23.6, 90);
       // Pose2d pose3 = new Pose2d(36, -12, Math.toRadians(150));
        //Pose2d pose4 = new Pose2d(36,-12.1,Math.toRadians(102.5));
       // Pose2d
        //Pose2d pose4 = new Pose2d(36, -12, Math.toRadians(90));


// x is -12.6 y is -33.3 heading - 139.9
        // --- NOTE: InitialPose2 is not needed and has been removed ---

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // --- PROBLEM: The old traj1, traj2, traj3 definitions are deleted ---
        // We now define ONE chained action instead:
            Action action1 = drive.actionBuilder(initialPose)
                    .setReversed(true)
                // 1. FIRST MOVEMENT: Go Forward 48 inches
                // We add 48 inches to the starting X-coordinate (34.8634 + 48)
                    .splineToLinearHeading(pose2, Math.toRadians(0))
                    //.splineToLinearHeading(pose4, Math.toRadians(0))
                    .build();


        Action action2 = drive.actionBuilder(pose2)
                .splineToLinearHeading(pose3, Math.toRadians(0))
                .build();
        //Action action2 = drive.actionBuilder(pose3)
               // .splineToLinearHeading(pose4,Math.toRadians(0))

                        //.build();
        //Action action3 = drive.actionBuilder(pose5)

        //Action action3 = drive.actionBuilder(pose3)
               // .splineToLinearHeading(pose4, Math.toRadians(0))

              //  .build();
        Action action3 = drive.actionBuilder(pose3)
                .splineToLinearHeading(pose4, Math.toRadians(0))
                .build();
        Action action4 = drive.actionBuilder(pose4)
                        .splineToLinearHeading(pose5, Math.toRadians(0))
                                .build();
        Action action5 = drive.actionBuilder(pose5)
                        .splineToLinearHeading(pose6, Math.toRadians(0))
                                .build();
        Action action6 = drive.actionBuilder(pose6)
                .splineToLinearHeading(pose7, Math.toRadians(0))
                        .build();
        Action action7 = drive.actionBuilder(pose2)
                        .splineToLinearHeading(pose4,Math.toRadians(0))
                                .build();









        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                startShooter(),
                                action1,
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                               new SleepAction(0.5),
                                transferDown(),
                               new SleepAction(0.5),
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                                new SleepAction(0.5),
                                transferDown(),
                               new SleepAction(0.5),
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                                new SleepAction(0.5),
                                transferDown(),
                                new SleepAction(0.5),
                                indexerToCollect(),
                                new SleepAction(0.5),
                                indexerToCollect(),
                                new SleepAction(0.5),
                                collect(),
                                new SleepAction(0.5),
                                action2,
                                new SleepAction(0.5),
                                action3,
                                new SleepAction(0.5),
                                indexerToCollect(),
                                action4,
                                indexerToCollect(),
                                action5,
                                stopCollect(),
                                new SleepAction(0.5),
                                action6,
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                                new SleepAction(0.5),
                                transferDown(),
                                new SleepAction(0.5),
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                                new SleepAction(0.5),
                                transferDown(),
                                new SleepAction(0.5),
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                                new SleepAction(0.5),
                                transferDown(),
                                new SleepAction(0.5),
                                action7,


                                indexerToTeleHome(),
                                new SleepAction(2.0)




                                // transfer functionn here

                        ),
                        indexerUpdate(),
                        updateShooter()
                )
        );





    }
    private Action stopCollect() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                intake.stop();
                return false;
            }
        };
    }
    private Action collect() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                intake.runIntake();
                return false;
            }
        };
    }
    private Action indexerToCollect() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                indexer.handleYButton();
                return false;
            }
        };
    }
    private Action indexerToTeleHome() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                indexer.indexerMotor.setTargetPosition(0);
                double errorThreshold = 1;
                return Math.abs(indexer.indexerMotor.getCurrentPosition()) > errorThreshold;
            }
        };
    }
    private Action indexerUpdate() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                indexer.update();
                return true;
            }
        };
    }
    private Action nextshoot() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                indexer.handleRightBumper();
                return false;
            }
        };
    }
    private Action transferUp() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (indexer.isAtShootPosition() && shooter.error <= 50) {
                    transfer.fire();
                }
                return false;
            }
        };
    }
    private Action transferDown() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                transfer.home();
                return false;
            }
        };
    }

    private Action startShooter() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                shooter.toggle();
                return false;
            }
        };
    }
    private Action updateShooter() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                shooter.setTargetRPM(RobotConstants.SHOOTER_RPM_NEAR);
                return true;
            }
        };
    }
}