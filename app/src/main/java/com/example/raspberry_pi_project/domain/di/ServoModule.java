package com.example.raspberry_pi_project.domain.di;

import com.example.raspberry_pi_project.domain.usecase.ServoUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;

@Module
@InstallIn(SingletonComponent.class)
public class ServoModule {
    @Provides
    public ServoUseCase provideServoUseCase(OkHttpClient client) {
        return new ServoUseCase(client);
    }
}
