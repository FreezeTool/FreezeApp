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
        Log.d(TAG, Log.getStackTraceString(e));
        Log.d(TAG, "----------------------------------------");
    }

    public static void error(String msg) {
        Log.e(TAG, msg);
    }

    public static void error(String msg, Throwable e) {
        Log.e(TAG, msg, e);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }
}
