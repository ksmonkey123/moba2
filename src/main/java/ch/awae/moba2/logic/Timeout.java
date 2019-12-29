package ch.awae.moba2.logic;

import ch.awae.utils.logic.Logic;

/**
 * Logic object that only yields true if {@link #start()} has been
 * called within a defined amount of time. A callback can be configured
 * to be called whenever the Timeout instance expires its time period.
 * <p>
 * A Timeout instance can be in 3 distinct states:
 * - 1. IDLE: the timeout is inactive and always evaluates to 'false'
 * - 2. RUNNING: the timeout is running, the timeout period has not yet expired. It will always yield 'true'
 * - 3. EXPIRED: the timeout is running, the timeout period has expired. It will yield 'false' and on the first evaluation it will transition back into the IDLE state.
 */
public class Timeout implements Logic {

    private final long timeout;
    private final Runnable onTimeout;

    private volatile long lastSet = -1;

    /**
     * create a new Timeout instance with the given time period and without
     * a callback
     *
     * @param millis the timeout period in milliseconds
     */
    public Timeout(long millis) {
        this(millis, () -> {
        });
    }

    /**
     * create a new Timeout instance with the given time period and a callback
     *
     * @param millis    the timeout period in milliseconds
     * @param onTimeout the callback to be called when the timeout period expires
     */
    public Timeout(long millis, Runnable onTimeout) {
        this.timeout = millis;
        this.onTimeout = onTimeout;
    }

    /**
     * start the timeout period.
     */
    public void start() {
        lastSet = System.currentTimeMillis();
    }

    /**
     * abort the timeout period. Transitions the instance silently back into the IDLE state ignoring the callback
     */
    public void stop() {
        lastSet = -1;
    }

    @Override
    public boolean evaluate() {
        if (lastSet == -1) {
            return false;
        }

        if (System.currentTimeMillis() - lastSet < timeout) {
            return true;
        } else {
            lastSet = -1;
            onTimeout.run();
            return false;
        }
    }
}
