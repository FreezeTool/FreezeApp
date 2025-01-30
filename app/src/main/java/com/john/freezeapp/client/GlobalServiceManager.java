package com.john.freezeapp.client;

import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.monitor.AppMonitorManager;
import com.john.freezeapp.util.UIExecutor;

public class GlobalServiceManager {

    public static void onDaemonBind(IDaemonBinder daemonBinder) {
        ClientLogBinderManager.registerClientLogBinder();
        UIExecutor.postDelay(new Runnable() {
            @Override
            public void run() {
                AppMonitorManager.registerTaskStackListener();
            }
        }, 1000);
    }

    public static void onDaemonUnbind() {

    }
}
