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
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.opmode.Drive;
import org.firstinspires.ftc.teamcode.opmode.Indexer;
import org.firstinspires.ftc.teamcode.opmode.Intake;
import org.firstinspires.ftc.teamcode.opmode.PinpointLocalizer;
import org.firstinspires.ftc.teamcode.opmode.Shooter;
import org.firstinspires.ftc.teamcode.opmode.Transfer;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Red Close Auto")
public class RedClose extends LinearOpMode {
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
        Pose2d initialPose = new Pose2d(-55.5, 44.5, Math.toRadians(135));
        Pose2d blueDriveToShootingPose = new Pose2d(-24, 24, Math.toRadians(135));
        Pose2d blueReadyToPickupFirstLinePose = new Pose2d(-9.5, 22.4, Math.toRadians(80));
        Pose2d bluePickupFirstLineFirstBallPose = new Pose2d(-9.5, 33.5, Math.toRadians(80));
        Pose2d bluePickupFirstLineSecondBallPose = new Pose2d(-9.5, 37.5, Math.toRadians(87.5));
        Pose2d bluePickupFirstLineThirdBallPose = new Pose2d(-9.5, 43.5, Math.toRadians(90));
        Pose2d blueShootFirstLinePose = new Pose2d(-24.1, 24, Math.toRadians(139));
        Pose2d pose6 = new Pose2d(10.5, 26, Math.toRadians(80));
        Pose2d pose7 = new Pose2d(10.5, 33.5, Math.toRadians(80));
        Pose2d pose8 = new Pose2d(11, 37.5, Math.toRadians(85));
        Pose2d pose9 = new Pose2d(11.25, 43, Math.toRadians(90));
        Pose2d blueDriveToShootingPose3 = new Pose2d(-24, 24, Math.toRadians(130));







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
        Action driveToShootPreload = drive.actionBuilder(initialPose)
                .setReversed(true)
                // 1. FIRST MOVEMENT: Go Forward 48 inches
                // We add 48 inches to the starting X-coordinate (34.8634 + 48)
                .splineToLinearHeading(blueDriveToShootingPose, Math.toRadians(0))
                //.splineToLinearHeading(pose4, Math.toRadians(0))
                .build();


        Action driveToPickupFirstLine = drive.actionBuilder(blueDriveToShootingPose)
                .splineToLinearHeading(blueReadyToPickupFirstLinePose, Math.toRadians(0))
                .build();
        //Action action2 = drive.actionBuilder(pose3)
        // .splineToLinearHeading(pose4,Math.toRadians(0))

        //.build();
        //Action action3 = drive.actionBuilder(pose5)

        //Action action3 = drive.actionBuilder(pose3)
        // .splineToLinearHeading(pose4, Math.toRadians(0))

        //  .build();
        Action driveToFirstLineFirstBall = drive.actionBuilder(blueReadyToPickupFirstLinePose)
                .splineToLinearHeading(bluePickupFirstLineFirstBallPose, Math.toRadians(0))
                .build();
        Action driveToFirstLineSecondBall = drive.actionBuilder(bluePickupFirstLineFirstBallPose)
                .splineToLinearHeading(bluePickupFirstLineSecondBallPose, Math.toRadians(0))
                .build();
        Action driveToFirstLineThirdBall = drive.actionBuilder(bluePickupFirstLineSecondBallPose)
                .splineToLinearHeading(bluePickupFirstLineThirdBallPose, Math.toRadians(0))
                .build();
        Action driveToShootFirstLine = drive.actionBuilder(bluePickupFirstLineThirdBallPose)
                .splineToLinearHeading(blueShootFirstLinePose, Math.toRadians(0))
                .build();
        Action action7 = drive.actionBuilder(blueDriveToShootingPose)
                .splineToLinearHeading(bluePickupFirstLineFirstBallPose,Math.toRadians(0))
                .build();
        Action action8 = drive.actionBuilder(blueDriveToShootingPose)
                .splineToLinearHeading(pose6, Math.toRadians(0))
                .build();
        Action action9 = drive.actionBuilder(pose6)
                .splineToLinearHeading(pose7, Math.toRadians(0))
                .build();
        Action action10 = drive.actionBuilder(pose7)
                .splineToLinearHeading(pose8, Math.toRadians(0))
                .build();
        Action action11 = drive.actionBuilder(pose8)
                .splineToLinearHeading(pose9, Math.toRadians(0))
                .build();
        Action action12 = drive.actionBuilder(pose9)
                .splineToLinearHeading(blueDriveToShootingPose3, Math.toRadians(0))
                .build();










        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                startShooter(),
                                driveToShootPreload,
                                nextshoot(),
                                new SleepAction(0.55),
                                transferUp(),
                                new SleepAction(0.25),
                                transferDown(),
                                new SleepAction(0.25),
                                nextshoot(),
                                new SleepAction(0.55),
                                transferUp(),
                                new SleepAction(0.25),
                                transferDown(),
                                new SleepAction(0.25),
                                nextshoot(),
                                new SleepAction(0.55),
                                transferUp(),
                                new SleepAction(0.35),
                                transferDown(),
                                new SleepAction(0.25),
                                indexerToCollect(),
                                new SleepAction(0.25),
                                indexerToCollect(),
                                new SleepAction(0.25),
                                collect(),

                                driveToPickupFirstLine,
                                new SleepAction(0.1),
                                driveToFirstLineFirstBall,
                                new SleepAction(0.25),
                                indexerToCollect(),
                                new SleepAction(0.25),
                                driveToFirstLineSecondBall,
                                new SleepAction(0.25),
                                indexerToCollect(),
                                driveToFirstLineThirdBall,
                                stopCollect(),
                                new SleepAction(0.35),
                                nextshoot(),
                                driveToShootFirstLine,

                                transferUp(),
                                new SleepAction(0.2),
                                transferDown(),
                                new SleepAction(0.2),
                                nextshoot(),
                                new SleepAction(0.55),
                                transferUp(),
                                new SleepAction(0.2),
                                transferDown(),
                                new SleepAction(0.2),
                                nextshoot(),
                                new SleepAction(0.55),
                                transferUp(),
                                new SleepAction(0.35),
                                transferDown(),
                                new SleepAction(0.2),
                                collect(),
                                indexerToCollect(),
                                new SleepAction(0.2),
                                action8,
                                action9,
                                new SleepAction(0.35),
                                indexerToCollect(),
                                new SleepAction(0.35),
                                action10,
                                new SleepAction(0.35),
                                indexerToCollect(),
                                new SleepAction(0.35),
                                action11,
                                new SleepAction(0.35),
                                indexerToCollect(),
                                new SleepAction(0.35),
                                action12,
                                new SleepAction(0.35),
                                nextshoot(),

                                new SleepAction(0.2),
                                transferUp(),
                                new SleepAction(0.2),
                                transferDown(),
                                new SleepAction(0.2),
                                nextshoot(),
                                new SleepAction(0.6),
                                transferUp(),
                                new SleepAction(0.2),
                                transferDown(),
                                new SleepAction(0.2),
                                nextshoot(),
                                new SleepAction(0.6),
                                transferUp(),
                                new SleepAction(0.2),
                                transferDown(),
                                new SleepAction(0.2),
                                nextshoot(),
                                new SleepAction(0.6),
                                transferUp(),
                                new SleepAction(0.2),
                                transferDown(),
                                action7,
                                indexerToTeleHome(),

                                new SleepAction(3)
















                                // transfer functionn here

                        ),
                        indexerUpdate(),
                        updateShooter(),
                        telemetryPacket -> {
                            telemetry.addData("indexer postion", indexer.currentTargetPosition);
                            telemetry.update();
                            return true;
                        }
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
                indexer.IndexerAutoToTele();
                return true;
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
                shooter.setTargetRPM(3225);
                return true;
            }
        };
    }
}