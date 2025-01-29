package com.john.freezeapp;

import android.app.Application;
import android.os.Build;

import com.john.freezeapp.util.FreezeUtil;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

public class App extends Application {

    public static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        if (FreezeUtil.atLeast28()) {
            HiddenApiBypass.addHiddenApiExemptions("L");
        }

        sApp = this;
    }

    public static App getApp() {
        return sApp;
    }
}
