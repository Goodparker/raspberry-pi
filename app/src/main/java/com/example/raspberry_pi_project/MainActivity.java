package com.example.raspberry_pi_project;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button btnLightOn, btnLightOff,btnServoOn,btnServoOff;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLightOn = findViewById(R.id.btnOn);
        btnLightOff = findViewById(R.id.btnOff);
        btnServoOn = findViewById(R.id.btnServoON);
        btnServoOff = findViewById(R.id.btnServoOFF);


        apiService = ApiClient.getClient().create(ApiService.class);

        btnLightOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLight("on");
            }
        });

        btnLightOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLight("off");
            }
        });

        btnServoOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {toggleServo("on");}
        });
        btnServoOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {toggleServo("off");}
        });

    }

    private void toggleLight(String state) {
        Call<Void> call = apiService.toggleLight(state);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Light turned " + state, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleServo(String state) {
        Call<Void> call = apiService.toggleServo(state);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Servo turned " + state, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
