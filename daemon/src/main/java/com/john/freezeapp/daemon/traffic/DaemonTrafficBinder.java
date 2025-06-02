package com.john.freezeapp.daemon.traffic;

import android.os.RemoteException;

public class DaemonTrafficBinder extends IDaemonTrafficBinder.Stub {
    @Override
    public void start(int threshold) throws RemoteException {
        try {
            TrafficMonitor.start(threshold);
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

    @Override
    public boolean isActive() throws RemoteException {
        return TrafficMonitor.isActive();
    }
}
