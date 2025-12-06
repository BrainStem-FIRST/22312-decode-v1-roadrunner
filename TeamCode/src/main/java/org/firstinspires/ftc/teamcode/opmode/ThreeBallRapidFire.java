package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Subsystem: ThreeBallRapidFire
 * Logic developed with AI assistance using the Team 22312 Protocol.
 * This is a Non-Blocking State Machine. It allows the driver to continue
 * driving the chassis while the robot automatically handles the
 * shoot-retract-index cycle 3 times.
 */
public class ThreeBallRapidFire {

    private final Shooter shooter;
    private final Indexer indexer;
    private final Transfer transfer;

    // State definitions
    private enum FireState {
        IDLE,
        PREP_CHECK,         // Ensure shooter is on and Indexer is in Shoot Mode
        WAIT_FOR_ALIGNMENT, // Wait for RPM and Indexer Position
        TRANSFER_KICK,      // Servo Up
        TRANSFER_RETRACT,   // Servo Down
        ADVANCE_INDEXER,    // Move to next slot
        COMPLETE
    }

    private FireState currentState = FireState.IDLE;
    private ElapsedTime timer = new ElapsedTime();
    private int shotsFired = 0;
    private final int TOTAL_SHOTS = 3;

    // Local timing variables (can be overridden by the Tuner)
    public long settleTimeMs = RobotConstants.AF_INDEXER_SETTLE_MS;
    public long upTimeMs = RobotConstants.AF_TRANSFER_UP_MS;
    public long downTimeMs = RobotConstants.AF_TRANSFER_DOWN_MS;

    public ThreeBallRapidFire(Shooter shooter, Indexer indexer, Transfer transfer) {
        this.shooter = shooter;
        this.indexer = indexer;
        this.transfer = transfer;
    }

    /**
     * Call this once to start the 3-ball sequence.
     * It immediately runs the first logic step to start motors ASAP.
     */
    public void startSequence() {
        // Only start if we aren't already running
        if (currentState == FireState.IDLE || currentState == FireState.COMPLETE) {
            shotsFired = 0;
            currentState = FireState.PREP_CHECK;

            // Call update immediately so the Indexer starts moving NOW
            // instead of waiting for the next loop cycle.
            update();
        }
    }

    /**
     * Call this immediately if the driver wants to cancel (e.g., safety).
     */
    public void abort() {
        transfer.home();
        currentState = FireState.IDLE;
    }

    /**
     * Helper to check if the sequence is active.
     * Use this in TeleOp to lock out other controls.
     */
    public boolean isRunning() {
        return currentState != FireState.IDLE && currentState != FireState.COMPLETE;
    }

    /**
     * Must be called in the TeleOp while(opModeIsActive) loop.
     * This handles the timing.
     */
    public void update() {

        switch (currentState) {
            case IDLE:
                // Do nothing
                break;

            case PREP_CHECK:
                // 1. Ensure Shooter is spinning. If not, turn it on (Far shot default)
                if (!shooter.isShooting()) {
                    shooter.setTargetRPM(RobotConstants.SHOOTER_RPM_FAR);
                    shooter.toggle(); // Turns it on
                }

                // 2. Ensure Indexer is in a "Shoot" state (Even).
                // If in "Collect" (Odd), advance it once immediately.
                if (!indexer.isAtShootPosition()) {
                    indexer.rapidFireRB(); // Moves from Odd -> Even (Shoot)
                }

                currentState = FireState.WAIT_FOR_ALIGNMENT;
                timer.reset();
                break;

            case WAIT_FOR_ALIGNMENT:
                // 3. Interlock: Check RPM and Indexer Position
                boolean shooterReady = shooter.isReadyToFire();
                // Check if indexer is in a 'shoot' (even) state and close to target
                boolean indexerReady = indexer.isAtShootPosition() &&
                        Math.abs(indexer.getIndexerError()) < 10; // 10 ticks tolerance

                // Also wait for the settle timer (for vibration)
                boolean settled = timer.milliseconds() > settleTimeMs;

                if (shooterReady && indexerReady && settled) {
                    currentState = FireState.TRANSFER_KICK;
                    timer.reset();
                }
                break;

            case TRANSFER_KICK:
                // 4. Fire!
                transfer.fire();

                // Wait for servo transit time
                if (timer.milliseconds() > upTimeMs) {
                    currentState = FireState.TRANSFER_RETRACT;
                    timer.reset();
                }
                break;

            case TRANSFER_RETRACT:
                // 5. Retract
                transfer.home();

                // Wait for servo return time
                if (timer.milliseconds() > downTimeMs) {
                    shotsFired++;
                    if (shotsFired >= TOTAL_SHOTS) {
                        currentState = FireState.COMPLETE;
                    } else {
                        currentState = FireState.ADVANCE_INDEXER;
                    }
                }
                break;

            case ADVANCE_INDEXER:
                // 6. Move Spindexer to next ball
                indexer.rapidFireRB(); // Moves from Shoot -> Shoot (skipping collect)

                // Go back to waiting for alignment
                timer.reset();
                currentState = FireState.WAIT_FOR_ALIGNMENT;
                break;

            case COMPLETE:
                // Sequence done.
                currentState = FireState.IDLE;
                break;
        }
    }

    public String getStatus() {
        return currentState.toString() + " | Shot: " + shotsFired;
    }
}