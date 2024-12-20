package com.example.raspberry_pi_project.domain.repository;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServoRepository {
    @GET("/servo/{state}")
    Call<Void> toggleServo(@Path("state") String state);
}
