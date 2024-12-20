package com.example.raspberry_pi_project.data.di;

import com.example.raspberry_pi_project.domain.repository.ServoRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ActivityComponent.class)
public class ApiModule {
    private static final String BASE_URL = "http://192.168.0.205:5000";

    @Provides
    public static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    public static ServoRepository provideServoRepository(
            Retrofit retrofit
    ) {
        return retrofit.create(ServoRepository.class);
    }
}
