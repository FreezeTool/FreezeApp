package com.john.freezeapp;

import android.app.Application;
import android.content.Context;

import com.john.freezeapp.monitor.AppMonitorManager;
import com.john.freezeapp.util.FreezeUtil;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

public class App extends Application {

    public static App sApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sApp = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (FreezeUtil.atLeast28()) {
            HiddenApiBypass.addHiddenApiExemptions("L");
        }
        AppMonitorManager.startAppMonitor(getApp());

    }

    public static App getApp() {
        return sApp;
    }
}
