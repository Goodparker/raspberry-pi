package com.example.raspberry_pi_project;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/light/{state}")
    Call<Void> toggleLight(@Path("state") String state);

    @GET("/servo/{state}")
    Call<Void> toggleServo(@Path("state") String state);
}