package com.example.raspberry_pi_project.domain.entities;

public class UpdateTimerEvent extends TimerEvent{
    private final int remainingTime;

    public UpdateTimerEvent(int remainingTime) {
        super(EventType.UPDATE);
        this.remainingTime = remainingTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}
