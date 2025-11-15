package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.CachingMotor;

import java.util.ArrayList;


public class BrainSTEMRobot {
    private Telemetry telemetry;
    private HardwareMap map;
    private boolean isAuto;

    DcMotorEx leftFrontDrive;


    public BrainSTEMRobot(Telemetry t, HardwareMap map){


        leftFrontDrive = new CachingMotor(map.get(DcMotorEx.class, "leftFrontDrive"));



    }

    public void motorOn() {
        leftFrontDrive.setPower(0.05);
    }

}
