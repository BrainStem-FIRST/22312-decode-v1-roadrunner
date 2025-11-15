package org.firstinspires.ftc.teamcode.opmode;

// Portions of this file’s structure were refactored with assistance from an LLM under mentor supervision.

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

/**
 * The Transfer subsystem manages the "lifter" servo that pushes
 * a game piece from the Indexer into the Shooter.
 */
public class Transfer {

    // Hardware
    public ServoImplEx lifter;

    /**
     * Constructor for the Transfer subsystem.
     * @param hwMap The hardware map from the OpMode.
     */
    public Transfer(HardwareMap hwMap) {
        init(hwMap);
    }

    /**
     * Initializes all Transfer hardware components.
     * @param hwMap The hardware map from the OpMode.
     */
    public void init(HardwareMap hwMap) {
        // Hardware mapping
        lifter = hwMap.get(ServoImplEx.class, "Lifter");

        // This is the "magic" from the original file.
        // It remaps the servo's physical PWM range (1620-1950)
        // to a logical range (0.0 - 1.0).
        lifter.setPwmRange(new PwmControl.PwmRange(

                RobotConstants.TRANSFER_LIFTER_DOWN_POS,   // This will be position 0.0
                RobotConstants.TRANSFER_LIFTER_RAISED_POS
        ));

        // Set the servo to its "home" position on init.
        home();
    }

    /**
     * Moves the servo to the "fire" position (1.0).
     */
    public void fire() {
        lifter.setPosition(0.99); // 1.0 is remapped to TRANSFER_LIFTER_RAISED_POS
    }

    /**
     * Moves the servo to the "home" position (0.0).
     */
    public void home() {
        lifter.setPosition(0.0); // 0.0 is remapped to TRANSFER_LIFTER_DOWN_POS
    }
}
