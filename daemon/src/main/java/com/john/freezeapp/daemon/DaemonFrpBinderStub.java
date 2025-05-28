package com.john.freezeapp.daemon;

import android.os.RemoteException;

import com.john.freezeapp.IFrpBinder;

public class DaemonFrpBinderStub extends IFrpBinder.Stub {
    @Override
    public boolean startFrpClient(String frpClientConfig) throws RemoteException {
        return false;
    }

    @Override
    public boolean startFrpServer(String frpServerConfig) throws RemoteException {
        return false;
    }
}
