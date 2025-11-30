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

@Autonomous(name = "Red Close Auto")
public class RedClose extends LinearOpMode {
    ServoImplEx lifter;
    Intake intake;
    Drive drive;

    Shooter shooter;
    Transfer transfer;
    Indexer indexer;
    PinpointLocalizer pinpoint;

    // --- New Transformation Logic ---

    /**
     * Transforms the coordinates for the Red Alliance based on the team's custom Pinpoint system:
     * 1. Swaps X and Y (Field X is Robot Y, Field Y is Robot X).
     * 2. Mirrors the final Y coordinate (Robot Y) and adjusts the heading.
     * * NOTE: Since your existing pose definitions are already mirrored from the Blue side
     * (e.g., -55.5, -44.5), this function assumes the input is the final Red target
     * and performs the X/Y swap and heading adjustment.
     */
    private Pose2d transformPoseForRed(Pose2d blueSidePose) {
        // Red Alliance: Y-axis is flipped (Y * -1) and X/Y are swapped relative to standard field coordinates.
        // Assuming the input poses are already the correct signed coordinates for the Red side.

        // 1. Swap X and Y: newX = oldY, newY = oldX
        double newX = blueSidePose.position.y;
        double newY = blueSidePose.position.x;

        // 2. Adjust heading: Mirror the angle (135 -> -135)
        // Since your input angles are already signed (-135), we just need to ensure
        // the angle is still valid for the new orientation.
        double newHeading = Math.toRadians(Math.toDegrees(blueSidePose.heading.log()) * -1);

        // Your current poses already look like they've been transformed for the Red side,
        // so we'll apply the X/Y swap and ensure the new Y is also signed correctly.
        return new Pose2d(newX, newY, blueSidePose.heading.log());
    }

    @Override
    public void runOpMode() throws InterruptedException {
        intake = new Intake(hardwareMap);
        drive = new Drive(hardwareMap);
//        pinpoint = new PinpointLocalizer(hardwareMap, new Pose2d(0, 0, 0), telemetry);
        shooter = new Shooter(hardwareMap);
        transfer = new Transfer(hardwareMap);
        indexer = new Indexer(hardwareMap);



        // --- APPLY TRANSFORMATION FOR RED ALLIANCE ---
        // This calculates the final positions by swapping X/Y and flipping the Y/heading
        // according to your custom odometry system's needs.
        Pose2d initialPose = new Pose2d(-55.5, 44.5, Math.toRadians(135));
        Pose2d pose2 = new Pose2d(-24, 24, Math.toRadians(135));
        Pose2d pose3 = new Pose2d(-9.5, 22.4, Math.toRadians(80));
        Pose2d pose4 = new Pose2d(-9.5, 33.5, Math.toRadians(80));
        Pose2d pose5 = new Pose2d(-9.5, 37.5, Math.toRadians(87.5));
        Pose2d pose6 = new Pose2d(-9.5, 43.5, Math.toRadians(90));
        Pose2d pose7 = new Pose2d(-24.1, 24, Math.toRadians(142));


        // --- Road Runner Initialization (drive object renamed for safety) ---
        MecanumDrive mecanumDrive = new MecanumDrive(hardwareMap, initialPose);

        // --- ACTION DEFINITIONS (Using the transformed Poses) ---

        Action action1 = mecanumDrive.actionBuilder(initialPose)
                .setReversed(true) // Retaining your setting
                .splineToLinearHeading(pose2, Math.toRadians(0))
                .build();


        Action action2 = mecanumDrive.actionBuilder(pose2)
                .splineToLinearHeading(pose3, Math.toRadians(0))
                .build();

        Action action3 = mecanumDrive.actionBuilder(pose3)
                .splineToLinearHeading(pose4, Math.toRadians(0))
                .build();
        Action action4 = mecanumDrive.actionBuilder(pose4)
                .splineToLinearHeading(pose5, Math.toRadians(0))
                .build();
        Action action5 = mecanumDrive.actionBuilder(pose5)
                .splineToLinearHeading(pose6, Math.toRadians(0))
                .build();
        Action action6 = mecanumDrive.actionBuilder(pose6)
                .splineToLinearHeading(pose7, Math.toRadians(0))
                .build();
        Action action7 = mecanumDrive.actionBuilder(pose2)
                .splineToLinearHeading(pose4,Math.toRadians(0))
                .build();

        // --- EXECUTION ---

        waitForStart();

        // Execution structure remains exactly as you wrote it
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
                        ),
                        indexerUpdate(),
                        updateShooter()
                )
        );
    }

    // --- Action Methods (Unchanged) ---
    private Action stopCollect() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { intake.stop(); return false; }
        };
    }
    private Action collect() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { intake.runIntake(); return false; }
        };
    }
    private Action indexerToCollect() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { indexer.handleYButton(); return false; }
        };
    }
    private Action indexerToTeleHome() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { indexer.IndexerAutoToTele(); return true; }
        };
    }
    private Action indexerUpdate() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { indexer.update(); return true; }
        };
    }
    private Action nextshoot() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { indexer.handleRightBumper(); return false; }
        };
    }
    private Action transferUp() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (indexer.isAtShootPosition() && shooter.error <= 50) {
                    transfer.fire();
                }
                return false;
            }
        };
    }
    private Action transferDown() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { transfer.home(); return false; }
        };
    }

    private Action startShooter() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { shooter.toggle(); return false; }
        };
    }
    private Action updateShooter() {
        return new Action() {
            @Override public boolean run(@NonNull TelemetryPacket telemetryPacket) { shooter.setTargetRPM(3275); return true; }
        };
    }
}