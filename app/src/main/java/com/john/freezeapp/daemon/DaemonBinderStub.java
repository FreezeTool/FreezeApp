package com.john.freezeapp.daemon;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.IRemoteProcess;
import com.john.freezeapp.daemon.process.RemoteProcess;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DaemonBinderStub extends IDaemonBinder.Stub {

    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

        if (code == android.os.IBinder.FIRST_CALL_TRANSACTION + 1) {
            data.enforceInterface(DESCRIPTOR);
            transactRemote(data, reply, flags);
            return true;
        }

        return super.onTransact(code, data, reply, flags);
    }

    private void transactRemote(Parcel data, Parcel reply, int flags) throws RemoteException {
        IBinder targetBinder = data.readStrongBinder();


        int targetCode = data.readInt();
        int targetFlags = data.readInt();

        DaemonLog.toClient(Binder.getCallingUid(), Binder.getCallingPid(), "call transactRemote descriptor=" + targetBinder.getInterfaceDescriptor() + ", code=" + targetCode + ", flags=" + targetFlags);
        Parcel newData = Parcel.obtain();
        try {
            newData.appendFrom(data, data.dataPosition(), data.dataAvail());
        } catch (Throwable tr) {
            return;
        }
        try {
            long id = Binder.clearCallingIdentity();
            targetBinder.transact(targetCode, newData, reply, targetFlags);
            Binder.restoreCallingIdentity(id);
        } finally {
            newData.recycle();
        }
    }

    @Override
    public String getConfig(String key) throws RemoteException {
        DaemonLog.toClient(Binder.getCallingUid(), Binder.getCallingPid(), "call getConfig key=" + key);
        switch (key) {
            case DaemonHelper.KEY_DAEMON_VERSION:
                return BuildConfig.VERSION_NAME;
//                return "1.0";
        }
        return "";
    }

    @Override
    public IRemoteProcess newProcess(String[] cmd, String[] env, String dir) throws RemoteException {
        DaemonLog.toClient(Binder.getCallingUid(), Binder.getCallingPid(), "call newProcess cmd=" + Arrays.toString(cmd));
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd, env, dir != null ? new File(dir) : null);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }

        return new RemoteProcess(process);
    }

    @Override
    public void closeDaemon() throws RemoteException {
        DaemonLog.toClient(Binder.getCallingUid(), Binder.getCallingPid(), "call closeDaemon");
        Daemon.getDaemon().stop();
    }

    @Override
    public IBinder getService(String name) throws RemoteException {
        DaemonLog.toClient(Binder.getCallingUid(), Binder.getCallingPid(), "call getService name=" + name);
        if (TextUtils.equals(name, Context.ACTIVITY_SERVICE)) {
            return DaemonService.activityManager.asBinder();
        } else if (TextUtils.equals(name, "package")) {
            return DaemonService.packageManager.asBinder();
        }
        return null;
    }

    @Override
    public boolean registerClientBinder(IBinder iClientBinder) throws RemoteException {
        DaemonClientBinderProxy.registerClientBinder(Binder.getCallingUid(), Binder.getCallingPid(), iClientBinder);
        return true;
    }

    @Override
    public void unregisterClientBinder(IBinder iClientBinder) throws RemoteException {
        DaemonClientBinderProxy.unregisterClientBinder(Binder.getCallingUid(), Binder.getCallingPid(), iClientBinder);
    }
}
