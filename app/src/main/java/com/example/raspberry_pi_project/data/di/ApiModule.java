package com.example.raspberry_pi_project.data.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {
    @Provides
    public OkHttpClient provideApiClient() {
        return new OkHttpClient();
    }
}
