package com.john.freezeapp.daemon;

import android.util.Log;

public class DaemonLog {
    public static final String TAG = "freeze-server";

    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(Throwable e, String msg) {
        Log.d(TAG, msg);
        Log.d(TAG, "----------------------------------------");
        Log.d(TAG,e.getMessage());
        Log.d(TAG, "----------------------------------------");
    }
}
