package com.john.freezeapp;

import android.app.Application;
import android.os.Build;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

public class App extends Application {

    public static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L");
        }

        sApp = this;
    }

    public static App getApp() {
        return sApp;
    }
}
