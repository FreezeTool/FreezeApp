package com.john.freezeapp.daemon.traffic;

import android.os.RemoteException;

public class DaemonTrafficBinder extends IDaemonTrafficBinder.Stub {
    @Override
    public void start() throws RemoteException {
        try {
            TrafficMonitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws RemoteException {
        try {
            TrafficMonitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
