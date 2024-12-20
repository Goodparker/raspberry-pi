package com.example.raspberry_pi_project.view;


import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.raspberry_pi_project.R;
import com.example.raspberry_pi_project.view.viewmodel.MainViewModel;
import com.github.lzyzsd.circleprogress.CircleProgress;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        CircleProgress progress = findViewById(R.id.circle_progress);
        ImageButton button = findViewById(R.id.toggleBtn);

        button.setOnClickListener(v -> {
            if (viewModel.isOpened()) {
                return;
            }
            viewModel.sendOpenEvent();
        });

        viewModel.getRemainingTime().observe(this, remainingTime -> {
            if (remainingTime < 0) {
                remainingTime = 0;
            }
            progress.setProgress(remainingTime);
        });
    }
}
