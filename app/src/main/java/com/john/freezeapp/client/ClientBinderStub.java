package com.john.freezeapp.client;

import android.os.IBinder;
import android.os.RemoteException;

import com.john.freezeapp.IClientBinder;

public class ClientBinderStub extends IClientBinder.Stub {

    @Override
    public IBinder getClient(String name) throws RemoteException {
        return null;
    }
}
