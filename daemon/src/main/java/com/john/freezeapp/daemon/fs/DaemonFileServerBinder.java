package com.john.freezeapp.daemon.fs;

import android.app.smartspace.uitemplatedata.Text;
import android.content.Context;
import android.os.RemoteException;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.widget.TextView;

import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.util.UIExecutor;
import com.john.freezeapp.util.DeviceUtil;

import java.io.File;

public class DaemonFileServerBinder extends IDaemonFileServer.Stub {
    FileServerManager fileServerManager = new FileServerManager();

    private static WindowManager mWM;

    @Override
    public boolean startServer() throws RemoteException {
        return fileServerManager.startServer();
    }

    @Override
    public boolean startFileServer(int port, String shareDir) throws RemoteException {
        return fileServerManager.startServer(port, new File(shareDir));
    }

    @Override
    public void stopServer() throws RemoteException {
        fileServerManager.stopServer();
    }

    @Override
    public boolean isActive() throws RemoteException {
        return fileServerManager.isRunning();
    }

    @Override
    public String getAccessUrl() throws RemoteException {
        return fileServerManager.getAccessUrl();
    }

    @Override
    public String getLocalIpAddress() throws RemoteException {
        return fileServerManager.getLocalIpAddress();
    }
}
