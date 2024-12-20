package com.example.raspberry_pi_project.domain.exception;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import okhttp3.HttpUrl;

public class FailedToggleServoException extends RuntimeException {
    private int code;
    private HttpUrl url;
    private String errorMsg;

    public FailedToggleServoException(
            int code, HttpUrl url, String errorMsg
    ) {
        super(constructMessage(code, url, errorMsg));
    }

    public FailedToggleServoException(int code,
                                      HttpUrl url,
                                      String errorMsg,
                                      Throwable cause) {

        super(constructMessage(code, url, errorMsg), cause);
        this.code = code;
        this.url = url;
        this.errorMsg = errorMsg;
    }

    @SuppressLint("DefaultLocale")
    public static String constructMessage(int code,
                                   HttpUrl url,
                                   String errorMsg) {
        return String.format(
                "Fail HTTP. CODE: %d, URL: %s. MSG: %s",
                code, url.toString(), errorMsg
        );
    }

    @NonNull
    @Override
    public String toString() {
        return constructMessage(code, url, errorMsg);
    }
}
