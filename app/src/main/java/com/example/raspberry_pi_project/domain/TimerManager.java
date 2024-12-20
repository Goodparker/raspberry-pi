package com.example.raspberry_pi_project.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.raspberry_pi_project.domain.entities.TimerEvent;
import com.example.raspberry_pi_project.domain.entities.UpdateTimerEvent;
import com.google.gson.Gson;

import java.util.TimerTask;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class TimerManager extends WebSocketListener {
    private final Timer timer;
    private static final java.util.Timer javaTimer = new java.util.Timer();
    private static final Integer TIMEOUT = 30;
    private static final Gson gson = new Gson();

    public TimerManager(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void onOpen(@NonNull WebSocket ws, @NonNull Response response) {
        super.onOpen(ws, response);
        TimerEvent startEvent = new TimerEvent(TimerEvent.EventType.START);
        ws.send(gson.toJson(startEvent));
        timer.update(TIMEOUT);
        scheduleDecrementTask();
    }

    private void scheduleDecrementTask() {
        javaTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timer.remainingTime() == 0) {
                    javaTimer.cancel();
                    return;
                }
                timer.decrement();
            }
        }, 0, 1000);
    }

    @Override
    public void onMessage(@NonNull WebSocket ws, @NonNull String text) {
        super.onMessage(ws, text);
        TimerEvent event = gson.fromJson(text, TimerEvent.class);
        switch (event.getType()) {
            case STOP:
                javaTimer.cancel();
                timer.reset();
            case UPDATE: {
                if (event instanceof UpdateTimerEvent) {
                    UpdateTimerEvent updateEvent = (UpdateTimerEvent) event;
                    int remainingTime = updateEvent.getRemainingTime();
                    timer.update(remainingTime);
                    javaTimer.cancel();
                    scheduleDecrementTask();
                }
            };
        }
    }

    @Override
    public void onClosing(@NonNull WebSocket ws, int code, @NonNull String reason) {
        super.onClosing(ws, code, reason);
        javaTimer.cancel();
        timer.reset();
    }

    @Override
    public void onFailure(@NonNull WebSocket ws, @NonNull Throwable t, @Nullable Response response) {
        super.onFailure(ws, t, response);
        javaTimer.cancel();
        timer.reset();
    }
}
