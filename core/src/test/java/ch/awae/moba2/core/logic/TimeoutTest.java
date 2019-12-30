package ch.awae.moba2.core.logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeoutTest {

    private static long TIMEOUT_PERIOD = 100;

    private Timeout timeout;
    private boolean callbackDone;

    @Before
    public void setup() {
        timeout = new Timeout(TIMEOUT_PERIOD, () -> callbackDone = true);
        callbackDone = false;
    }

    @Test
    public void testIdleTimeoutYieldsFalse() {
        assertFalse(timeout.evaluate());
        assertFalse(callbackDone);
    }

    @Test
    public void testActiveTimeoutYieldsTrue() throws InterruptedException {
        assertFalse(timeout.evaluate());
        assertFalse(callbackDone);

        timeout.start();
        Thread.sleep(TIMEOUT_PERIOD / 2);
        assertTrue(timeout.evaluate());
        assertFalse(callbackDone);
    }

    @Test
    public void testExpiredTimeoutCallsCallbackAndYieldsFalse() throws InterruptedException {
        assertFalse(timeout.evaluate());
        assertFalse(callbackDone);

        timeout.start();
        Thread.sleep(TIMEOUT_PERIOD * 3 / 2);
        assertFalse(timeout.evaluate());
        assertTrue(callbackDone);
    }

    @Test
    public void testStoppedTimeoutIgnoresCallback() throws InterruptedException {
        assertFalse(timeout.evaluate());
        assertFalse(callbackDone);

        timeout.start();
        Thread.sleep(TIMEOUT_PERIOD / 2);
        timeout.stop();
        assertFalse(timeout.evaluate());
        assertFalse(callbackDone);
        Thread.sleep(TIMEOUT_PERIOD);
        assertFalse(callbackDone);
        timeout.stop();
    }





}
