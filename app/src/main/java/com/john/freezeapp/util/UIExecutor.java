package com.john.freezeapp.util;

import android.os.Handler;
import android.os.Looper;


public class UIExecutor {
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        mHandler.post(runnable);
    }


    public static void postDelay(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }


    public static void postUI(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

}
