package com.john.freezeapp.monitor;

import android.os.IBinder;
import android.os.RemoteException;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.monitor.IDaemonAppMonitorBinder;

public class AppMonitor {
    public static boolean start() {
        IDaemonAppMonitorBinder binder = getBinder();
        if (binder != null) {
            try {
                return binder.start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean stop() {
        IDaemonAppMonitorBinder binder = getBinder();
        if (binder != null) {
            try {
                return binder.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isActive() {
        IDaemonAppMonitorBinder binder = getBinder();
        if (binder != null) {
            try {
                return binder.isActive();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getSize() {
        IDaemonAppMonitorBinder binder = getBinder();
        if (binder != null) {
            try {
                return binder.getSize();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void updateSize(int size) {
        IDaemonAppMonitorBinder binder = getBinder();
        if (binder != null) {
            try {
                binder.updateSize(size);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }




    public static IDaemonAppMonitorBinder getBinder() {
        try {
            IBinder service = ClientBinderManager.getDaemonBinder().getService(DaemonHelper.DAEMON_BINDER_APP_MONITOR);
            return IDaemonAppMonitorBinder.Stub.asInterface(service);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
