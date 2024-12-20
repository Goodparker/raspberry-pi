package com.example.raspberry_pi_project.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> remainingTime;

    @Inject
    public MainViewModel() {

    }

    public synchronized boolean isOpened() {
        return Optional.ofNullable(remainingTime.getValue())
                .map(time -> time > 0)
                .orElse(false);
    }

    public synchronized void sendOpenEvent() {
        Log.d("toggle_btn", "Clicked open serve button");
    }

    public LiveData<Integer> getRemainingTime() {
        if (remainingTime == null) {
            remainingTime = new MutableLiveData<>(0);
        }
        return remainingTime;
    }
}