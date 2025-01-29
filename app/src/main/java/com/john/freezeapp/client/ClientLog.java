package com.john.freezeapp.client;

import android.util.Log;

public class ClientLog {
    public static final String TAG = "freeze-client";
    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(Throwable e, String msg) {
        Log.d(TAG, msg);
        Log.d(TAG, "----------------------------------------");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            Log.d(TAG, stackTraceElement.toString());
        }
        Log.d(TAG, "----------------------------------------");
    }
}
