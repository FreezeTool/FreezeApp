package com.john.freezeapp.fs;

import android.os.IBinder;
import android.os.RemoteException;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.fs.IDaemonFileServer;

public class ClientFileServer {
    public static IDaemonFileServer getFileServerBinder() {
        try {
            IBinder service = ClientBinderManager.getDaemonBinder().getService(DaemonHelper.DAEMON_BINDER_FILE_SERVER);
            return IDaemonFileServer.Stub.asInterface(service);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean startServer() {
        IDaemonFileServer fileServerBinder = getFileServerBinder();

        if (fileServerBinder != null) {
            try {
                return fileServerBinder.startServer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static void stopServer() {
        IDaemonFileServer fileServerBinder = getFileServerBinder();

        if (fileServerBinder != null) {
            try {
                fileServerBinder.stopServer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getAccessUrl() {
        IDaemonFileServer fileServerBinder = getFileServerBinder();

        if (fileServerBinder != null) {
            try {
                return fileServerBinder.getAccessUrl();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    public static boolean isActive() {
        IDaemonFileServer fileServerBinder = getFileServerBinder();

        if (fileServerBinder != null) {
            try {
                return fileServerBinder.isActive();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;

    }


    public static String getLocalIpAddress() {
        IDaemonFileServer fileServerBinder = getFileServerBinder();

        if (fileServerBinder != null) {
            try {
                return fileServerBinder.getLocalIpAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
