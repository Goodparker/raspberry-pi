package com.example.raspberry_pi_project;

import com.example.raspberry_pi_project.domain.common.timer.Timer;
import com.example.raspberry_pi_project.domain.common.timer.TimerManager;
import com.example.raspberry_pi_project.domain.entities.TimerEvent;
import com.example.raspberry_pi_project.domain.entities.UpdateTimerEvent;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import okhttp3.WebSocket;

import static org.mockito.Mockito.*;

public class TimerManagerTest {
    private Timer mockTimer;
    private WebSocket mockWebSocket;
    private TimerManager timerManager;
    private Gson gson;

    @Before
    public void setUp() {
        mockTimer = mock(Timer.class);
        mockWebSocket = mock(WebSocket.class);
        timerManager = new TimerManager(mockTimer);
        gson = new Gson();
    }

    @Test
    public void testSendStartEventStartsTimer() {
        when(mockTimer.isRunning()).thenReturn(false);
        when(mockWebSocket.send(anyString())).thenReturn(true);
        when(mockTimer.start(anyInt())).thenReturn(true);

        timerManager.sendStartEvent(mockWebSocket);

        verify(mockWebSocket, times(1)).send(contains("\"type\":\"START\""));
        verify(mockTimer, times(1)).start(anyInt());
    }

    @Test
    public void testSendStartEventDoesNothingIfTimerIsRunning() {
        when(mockTimer.isRunning()).thenReturn(true);

        timerManager.sendStartEvent(mockWebSocket);

        verify(mockWebSocket, never()).send(anyString());
        verify(mockTimer, never()).start(anyInt());
    }

    @Test
    public void testStartTimerInternalFailsIfWebSocketCannotSend() {
        when(mockWebSocket.send(anyString())).thenReturn(false);

        timerManager.startTimerInternal(mockWebSocket);

        verify(mockWebSocket, times(1)).send(contains("\"type\":\"START\""));
        verify(mockTimer, never()).start(anyInt());
    }

    @Test
    public void testStartTimerInternalClosesWebSocketIfTimerFailsToStart() {
        when(mockWebSocket.send(anyString())).thenReturn(true);
        when(mockTimer.start(anyInt())).thenReturn(false);

        timerManager.startTimerInternal(mockWebSocket);

        verify(mockWebSocket, times(1)).send(contains("\"type\":\"START\""));
        verify(mockWebSocket, times(1)).close(1011, "Failed to start client Timer");
    }

    @Test
    public void testOnMessageStopsTimerOnStopEvent() {
        TimerEvent stopEvent = new TimerEvent(TimerEvent.EventType.STOP);
        String stopEventJson = gson.toJson(stopEvent);

        timerManager.onMessage(mockWebSocket, stopEventJson);

        verify(mockTimer, times(1)).stop();
    }

    @Test
    public void testOnMessageResetsTimerOnUpdateEvent() {
        UpdateTimerEvent updateEvent = new UpdateTimerEvent( 5);
        String updateEventJson = gson.toJson(updateEvent);

        timerManager.onMessage(mockWebSocket, updateEventJson);

        verify(mockTimer, times(1)).reset(5);
    }

    @Test
    public void testOnMessageIgnoresInvalidEvent() {
        String invalidEventJson = "{\"type\":\"INVALID\"}";

        timerManager.onMessage(mockWebSocket, invalidEventJson);

        verify(mockTimer, never()).stop();
        verify(mockTimer, never()).reset(anyInt());
    }
}
