package com.john.freezeapp.daemon.fs;

import android.os.RemoteException;
import android.view.WindowManager;

import java.io.File;

public class DaemonFileServerBinder extends IDaemonFileServer.Stub {
    FileServerManager fileServerManager = new FileServerManager();

    private static WindowManager mWM;

    @Override
    public boolean startServer() throws RemoteException {
        try {
            return fileServerManager.startServer();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean startFileServer(int port, String shareDir) throws RemoteException {
        try {
            return fileServerManager.startServer(port, new File(shareDir));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void stopServer() throws RemoteException {
        try {
            fileServerManager.stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isActive() throws RemoteException {
        try {
            return fileServerManager.isRunning();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getAccessUrl() throws RemoteException {
        try {
            return fileServerManager.getAccessUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getLocalIpAddress() throws RemoteException {
        try {
            return fileServerManager.getLocalIpAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
