package com.example.raspberry_pi_project.domain.common.timer;

import androidx.lifecycle.LiveData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer extends LiveData<Integer> {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;

    public static final Integer ZERO_TIME = 0;
    private boolean isRunning = false;

    public Timer() {
        super(ZERO_TIME);
    }

    public synchronized boolean start(int timeout) {
        if (isRunning) {
            return false;
        }
        postValue(timeout);
        scheduledFuture = executor.scheduleWithFixedDelay(
                this::decrementRemainingTime,
                0, 1, TimeUnit.SECONDS
        );
        isRunning = true;
        return true;
    }

    public synchronized boolean stop() {
        if (!isRunning || scheduledFuture == null || scheduledFuture.isDone()) {
            return false;
        }
        if (!scheduledFuture.cancel(true)) {
            throw new FailedToCancelScheduledFutureException();
        }
        scheduledFuture = null;
        isRunning = false;
        postValue(ZERO_TIME);
        return true;
    }

    private void decrementRemainingTime() {
        synchronized (this) {
            Integer remainingTime = getValue();
            if (remainingTime == null || remainingTime <= 0) {
                stop();
                return;
            }
            postValue(remainingTime - 1);
        }
    }

    public synchronized boolean reset(int timeout) {
        if (!isRunning) {
            return false;
        }
        stop();
        return start(timeout);
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }
}
