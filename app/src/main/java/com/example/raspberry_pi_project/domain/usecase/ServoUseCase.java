package com.example.raspberry_pi_project.domain.usecase;

import com.example.raspberry_pi_project.domain.Timer;
import com.example.raspberry_pi_project.domain.TimerManager;
import com.example.raspberry_pi_project.domain.entities.TimerEvent;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class ServoUseCase {
    private Timer timer;
    private TimerManager manager;
    private final OkHttpClient client;
    private WebSocket webSocket;

    public ServoUseCase(OkHttpClient client) {
        this.client = client;
    }

    private static final String WS_URL = "ws://192.168.0.8:8080/ws";

    public void toggleServo() {
        Timer timer = getTimer();
        Request req = new Request.Builder()
                .url(WS_URL)
                .build();
        manager = new TimerManager(timer);
        if (webSocket == null) {
            webSocket = client.newWebSocket(req, manager);
        }
    }

    public Timer getTimer() {
        if (timer == null) {
            timer = new Timer(0);
        }
        return timer;
    }
}
