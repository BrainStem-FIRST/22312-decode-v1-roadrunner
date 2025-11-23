package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Vision {

    private Limelight3A limelight;
    private LLResult result;

    public Vision(HardwareMap hwMap) {
        // Initialize our Mock Driver
        limelight = new Limelight3A();
        limelight.start();

        // NOTE: You must set Pipeline 0 to AprilTags in the Web UI manually!
        limelight.pipelineSwitch(0);
    }

    /**
     * Call this at the start of every loop!
     */
    public void update() {
        result = limelight.getLatestResult();
    }

    public double getAlignTurnPower() {
        if (result == null || !result.isValid()) {
            return 0.0;
        }

        double tx = result.getTx();

        if (Math.abs(tx) < RobotConstants.VISION_TOLERANCE_DEGREES) {
            return 0.0;
        }

        double turnPower = -tx * RobotConstants.VISION_TURN_KP;

        if (Math.abs(turnPower) > RobotConstants.VISION_MAX_TURN_SPEED) {
            turnPower = Math.signum(turnPower) * RobotConstants.VISION_MAX_TURN_SPEED;
        }

        return turnPower;
    }

    public boolean hasTarget() {
        return result != null && result.isValid();
    }

    public double getError() {
        return (result != null) ? result.getTx() : 0.0;
    }

    public void printInfo(Telemetry telemetry) {
        if (result != null) {
            telemetry.addData("LL Valid", result.isValid());
            telemetry.addData("LL tx", result.getTx());
        } else {
            telemetry.addData("LL Status", "No Data");
        }
    }

    public void stop() {
        limelight.stop();
    }
}