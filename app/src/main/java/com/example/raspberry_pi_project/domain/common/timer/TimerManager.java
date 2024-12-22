package com.example.raspberry_pi_project.domain.common.timer;

import androidx.annotation.NonNull;

import com.example.raspberry_pi_project.domain.entities.TimerEvent;
import com.example.raspberry_pi_project.domain.entities.UpdateTimerEvent;
import com.google.gson.Gson;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class TimerManager extends WebSocketListener {
    private final Timer timer;
    private final Gson gson = new Gson();
    private static final Integer TIMEOUT = 10;


    public TimerManager(Timer timer) {
        this.timer = timer;
    }

    public void sendStartEvent(WebSocket ws) {
        if (timer.isRunning()) {
            return;
        }
        startTimerInternal(ws);
    }

    public void startTimerInternal(WebSocket ws) {
        TimerEvent startEvent = new TimerEvent(TimerEvent.EventType.START);
        if (!ws.send(gson.toJson(startEvent))) {
            return;
        }
        if (!timer.start(TIMEOUT)) {
            ws.close(1011, "Failed to start client Timer");
        }
    }

    @Override
    public void onMessage(@NonNull WebSocket ws, @NonNull String text) {
        super.onMessage(ws, text);
        TimerEvent obtainedEvent = gson.fromJson(text, TimerEvent.class);
        switch (obtainedEvent.getType()) {
            case STOP:
                timer.stop();
            case UPDATE: {
                if (obtainedEvent instanceof UpdateTimerEvent) {
                    UpdateTimerEvent updateEvent = (UpdateTimerEvent) obtainedEvent;
                    timer.reset(updateEvent.getRemainingTime());
                }
            }
        }
    }
}
