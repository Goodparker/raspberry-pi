package com.example.raspberry_pi_project.domain.entities;

public class TimerEvent {
    public enum EventType {
        START,
        UPDATE,
        STOP
    }

    private final EventType type;

    public TimerEvent(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
