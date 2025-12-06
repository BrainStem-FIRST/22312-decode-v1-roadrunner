package org.firstinspires.ftc.teamcode.opmode.autos;

import androidx.annotation.NonNull;

//import com.acmerobotics.dashboard.config.Config;
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

@Autonomous(name = "Blue Close Three Gate Three")
//@Config
public class BlueCloseThreeGateThree extends LinearOpMode {
    public static double gateWaitTime = 2.5;
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
        Pose2d blueDriveToShootingPose = new Pose2d(-24, -24, Math.toRadians(-135));
        Pose2d blueReadyToPickupFirstLinePose = new Pose2d(-9.5, -22.4, Math.toRadians(-80));
        Pose2d bluePickupFirstLineFirstBallPose = new Pose2d(-9.5, -33.5, Math.toRadians(-80));
        Pose2d bluePickupFirstLineSecondBallPose = new Pose2d(-9.5, -37.5, Math.toRadians(-87.5));
        Pose2d bluePickupFirstLineThirdBallPose = new Pose2d(-9.5, -43.5, Math.toRadians(-90));
        Pose2d blueShootFirstLinePose = new Pose2d(-21.5, -24, Math.toRadians(-142));
        Pose2d leaveGate = new Pose2d(-20, -57, Math.toRadians(0));
        Pose2d goToGate = new Pose2d(-2, -47, Math.toRadians(0));
        Pose2d atGate = new Pose2d(1.2, -61.5, Math.toRadians(0));
        Pose2d blueDriveToShootingPose3 = new Pose2d(24, 24, Math.toRadians(-130));







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
        Action driveToShootFirstLine = drive.actionBuilder(atGate)
                .setReversed(true)
                .splineToLinearHeading(blueShootFirstLinePose, Math.toRadians(0))
                .build();

        Action driveToGate = drive.actionBuilder(bluePickupFirstLineThirdBallPose)
                .splineToLinearHeading(goToGate, Math.toRadians(0))
                .build();
        Action park = drive.actionBuilder(blueShootFirstLinePose)
                .splineToLinearHeading(leaveGate, Math.toRadians(0))
                .build();
        Action openGate = drive.actionBuilder(goToGate)
                .splineToLinearHeading(atGate, Math.toRadians(0))
                .build();











        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                startShooter(),

                                driveToShootPreload,
                                new SleepAction(1),
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
                                new SleepAction(0.25),
                                driveToFirstLineThirdBall,
                                stopCollect(),
                                new SleepAction(0.35),

                                nextshoot(),
                                driveToGate,
                                new SleepAction(0.1),
                                openGate,
                                new SleepAction(2.5),
                                driveToShootFirstLine,
                                new SleepAction(0.3),


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
                                park,





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
                shooter.setTargetRPM(3150);
                return true;
            }
        };
    }
}