package com.example.raspberry_pi_project.domain.usecase;

import com.example.raspberry_pi_project.domain.repository.ServoRepository;

import java.util.concurrent.Flow;

public class ServoUseCase {
    private final ServoRepository servoRepository;

    public ServoUseCase(ServoRepository servoRepository) {
        this.servoRepository = servoRepository;
    }
}
