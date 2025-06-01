package com.john.freezeapp.traffic;

import android.os.IBinder;
import android.os.RemoteException;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.traffic.IDaemonTrafficBinder;

public class ClientTrafficMonitor {

    private static IDaemonTrafficBinder getDaemonTrafficBinder() {
        try {
            IBinder service = ClientBinderManager.getDaemonBinder().getService(DaemonHelper.DAEMON_BINDER_TRAFFIC_MONITOR);
            if (service != null) {
                return IDaemonTrafficBinder.Stub.asInterface(service);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void start() {
        IDaemonTrafficBinder daemonTrafficBinder = getDaemonTrafficBinder();
        if (daemonTrafficBinder != null) {
            try {
                daemonTrafficBinder.start();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void stop() {
        IDaemonTrafficBinder daemonTrafficBinder = getDaemonTrafficBinder();
        if (daemonTrafficBinder != null) {
            try {
                daemonTrafficBinder.stop();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void getHistory() {

    }
}
