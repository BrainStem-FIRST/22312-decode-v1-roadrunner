package org.firstinspires.ftc.teamcode.opmode.autos;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
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

@Autonomous(name = "RedFarAuto")
public class RedFarAuto extends LinearOpMode {
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
        Pose2d initialPose = new Pose2d(61.1, 15.1, Math.toRadians(180));
        Pose2d pose2 = new Pose2d(55, 14.8, Math.toRadians(146));
        Pose2d pose3 = new Pose2d(36.103, 36.25, Math.toRadians(90));
        Pose2d pose4 = new Pose2d(36.102,40, Math.toRadians(90));
        Pose2d pose5 = new Pose2d(36.101, 44.5, Math.toRadians(90));
        Pose2d supportingPose = new Pose2d(36.1, 31.5, Math.toRadians(90));
        Pose2d endingPose = new Pose2d(35, 20, Math.toRadians(180));





        //  Pose2d pose4 = new Pose2d(-12.3, 23.6, 90);
        // Pose2d pose3 = new Pose2d(36, -12, Math.toRadians(150));
        //Pose2d pose4 = new Pose2d(36,-12.1,Math.toRadians(102.5));
        // Pose2d
        //Pose2d pose4 = new Pose2d(36, -12, Math.toRadians(90));


// x is -12.6 y is -33.3 heading - 139.9
        // --- NOTE: InitialPose2 is not needed and has been removed ---
        //x is 9.4 y is 34.7 headingis -90

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
        Action action2 = drive.actionBuilder(supportingPose)
                .splineToLinearHeading(pose3, Math.toRadians(0))
                .build();
        Action action3 = drive.actionBuilder(pose3)
                .splineToLinearHeading(pose4, Math.toRadians(0))
                .build();
        Action action4 = drive.actionBuilder(pose4)
                .splineToLinearHeading(pose5, Math.toRadians(0))
                .build();
        Action supportingAction = drive.actionBuilder(pose2)
                .splineToLinearHeading(supportingPose, Math.toRadians(0))
                .build();
        Action action5 = drive.actionBuilder(pose5)
                .splineToLinearHeading(initialPose, Math.toRadians(0), new TranslationalVelConstraint((30)))
                .build();
        Action action6 = drive.actionBuilder(initialPose)
                .splineToLinearHeading(pose2, Math.toRadians(0), new TranslationalVelConstraint(30))
                .build();
        Action action7 = drive.actionBuilder(pose2)
                .splineToLinearHeading(endingPose, Math.toRadians(0), new TranslationalVelConstraint(30))
                .build();











// x is 57.7 y is-16.5 heading is -180
        //2nd is 61.1 for x -15.1 for y heading is -180
        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(

                                startShooter(),
                                action1,
                                nextshoot(),
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
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                                new SleepAction(0.5),
                                transferDown(),
                                new SleepAction(0.5),
                                collect(),
                                indexerToCollect(),
                                new SleepAction(0.5),
                                supportingAction,
                                new SleepAction(0.5),
                                action2,
                                new SleepAction(0.5),
                                indexerToCollect(),
                                new SleepAction(0.5),
                                action3,
                                new SleepAction(0.5),
                                indexerToCollect(),
                                new SleepAction(0.5),
                                action4,
                                new SleepAction(0.5),
                                indexerToCollect(),
                                new SleepAction(0.5),
                                action5,
                                new SleepAction(0.5),
                                action6,
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
                                nextshoot(),
                                new SleepAction(0.5),
                                transferUp(),
                                new SleepAction(0.5),
                                transferDown(),
                                new SleepAction(0.5),
                                action7,




















                                new SleepAction(1),
                                indexerToTeleHome()





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
                indexer.rapidFireRB();
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
                shooter.setTargetRPM(4075);
                return true;
            }
        };
    }
}

