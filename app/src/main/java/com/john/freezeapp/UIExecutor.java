package com.john.freezeapp;

import android.os.Handler;
import android.os.Looper;


public class UIExecutor {
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}
