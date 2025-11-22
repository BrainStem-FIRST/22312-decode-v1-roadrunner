package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Transfer {

    // Hardware
    public ServoImplEx lifter;

    public Transfer(HardwareMap hwMap) {
        init(hwMap);
    }

    public void init(HardwareMap hwMap) {
        lifter = hwMap.get(ServoImplEx.class, "Lifter");

        // Remap using the constants (Tune these in RobotConstants.java!)
        lifter.setPwmRange(new PwmControl.PwmRange(
                RobotConstants.TRANSFER_LIFTER_DOWN_POS,
                RobotConstants.TRANSFER_LIFTER_RAISED_POS
        ));

        home();
    }

    public void fire() {
        lifter.setPosition(0.99); // Moves to TRANSFER_LIFTER_RAISED_POS
    }

    public void home() {
        lifter.setPosition(0.0); // Moves to TRANSFER_LIFTER_DOWN_POS
    }
}