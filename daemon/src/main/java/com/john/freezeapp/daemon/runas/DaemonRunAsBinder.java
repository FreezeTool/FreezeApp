package com.john.freezeapp.daemon.runas;

import android.os.RemoteException;

import com.john.freezeapp.daemon.util.ProcessUtils;

import java.util.List;

public class DaemonRunAsBinder extends IRunAsBinder.Stub {
    @Override
    public void start(String classPath, String packageName) throws RemoteException {
        RunAsManager.startRunAs(classPath, packageName);
    }

    @Override
    public void stop(String packageName) throws RemoteException {
        RunAsManager.stopRunAs(packageName);
    }

    @Override
    public List<RunAsProcessModel> getActiveRunAsProcess() throws RemoteException {
        return ProcessUtils.getRunAsAppProcess();
    }

    @Override
    public List<RunAsProcessModel> getRunAsProcess() throws RemoteException {
        return RunAsManager.getRunAsProcess();
    }
}
