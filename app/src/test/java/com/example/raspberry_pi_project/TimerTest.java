package com.example.raspberry_pi_project;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.raspberry_pi_project.domain.common.timer.Timer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TimerTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Timer timer;
    private Observer<Integer> observer;

    @Before
    public void setUp() {
        timer = new Timer();
        observer = value -> {};
        timer.observeForever(observer);
    }

    @Test
    public void testStartTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        timer.observeForever(value -> {
            if (value != null) {
                latch.countDown();
            }
        });

        boolean started = timer.start(3);
        assertTrue("Timer should start successfully", started);
        assertTrue("Timer should be running", timer.isRunning());
        latch.await(100, TimeUnit.SECONDS); // Wait for timer to finish
        assertEquals("Timer should end at 0", Timer.ZERO_TIME, timer.getValue());
    }

    @Test
    public void testStopTimer() throws InterruptedException {
        timer.start(5);
        assertTrue("Timer should be running", timer.isRunning());

        boolean stopped = timer.stop();
        assertTrue("Stopping the timer should return true", stopped);
        assertFalse("Timer should not be running", timer.isRunning());
        assertEquals("Timer value should reset to 0 after stopping", Timer.ZERO_TIME, timer.getValue());
    }

    @Test
    public void testResetTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        timer.observeForever(value -> {
            if (value != null) {
                latch.countDown();
            }
        });

        timer.start(5);
        assertTrue("Timer should be running after start", timer.isRunning());

        boolean reset = timer.reset(3);
        assertTrue("Resetting the timer should return true", reset);
        assertTrue("Timer should still be running after reset", timer.isRunning());

        latch.await(4, TimeUnit.SECONDS); // Wait for the new countdown
        assertEquals("Timer should reset and count down to 0", Timer.ZERO_TIME, timer.getValue());
    }

    @Test
    public void testStartTimerAlreadyRunning() {
        timer.start(5);
        boolean startedAgain = timer.start(10);
        assertFalse("Starting a running timer should return false", startedAgain);
    }

    @Test
    public void testStopTimerWhenNotRunning() {
        boolean stopped = timer.stop();
        assertFalse("Stopping a timer that is not running should return false", stopped);
    }

    @Test
    public void testResetTimerWhenNotRunning() {
        boolean reset = timer.reset(5);
        assertFalse("Resetting a timer that is not running should return false", reset);
    }
}
