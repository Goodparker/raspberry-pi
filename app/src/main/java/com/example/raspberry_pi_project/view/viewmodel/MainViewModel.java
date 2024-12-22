package com.example.raspberry_pi_project.view.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.raspberry_pi_project.domain.common.timer.Timer;
import com.example.raspberry_pi_project.domain.usecase.ServoUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private ServoUseCase servoUseCase;

    @Inject
    public MainViewModel(ServoUseCase servoUseCase) {
        this.servoUseCase = servoUseCase;
    }

    public void toggleServo() {
        servoUseCase.toggleServo();
    }

    public Timer getTimer() {
        return servoUseCase.getTimer();
    }
}