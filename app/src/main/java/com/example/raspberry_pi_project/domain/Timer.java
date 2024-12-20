package com.example.raspberry_pi_project.domain;

import androidx.lifecycle.LiveData;

import java.util.Objects;

public class Timer extends LiveData<Integer> {
    public static final Integer ZERO_TIME = 0;
    private final Integer timout;

    public int getTimeout() {
        return timout;
    }

    public Timer(int remainingTime) {
        super(remainingTime);
        this.timout = remainingTime;
    }

    public void update(int remainingTime) {
        this.postValue(remainingTime);
    }

    public void reset() {
        this.postValue(ZERO_TIME);
    }

    public Integer remainingTime() {
        return this.getValue();
    }

    public boolean hasRemainingTime() {
        Objects.requireNonNull(getValue());
        return getValue() > ZERO_TIME;
    }

    public void decrement() {
        Objects.requireNonNull(getValue());
        this.postValue(getValue() - 1);
    }
}
