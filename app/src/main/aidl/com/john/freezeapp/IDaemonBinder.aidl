// IDaemonBinder.aidl
package com.john.freezeapp;

// Declare any non-default types here with import statements
import com.john.freezeapp.IRemoteProcess;
import com.john.freezeapp.IClientBinder;
import android.os.IBinder;
interface IDaemonBinder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getConfig(String key) = 2;

    IRemoteProcess newProcess(in String[] cmd, in String[] env, in String dir) = 3;

    void closeDaemon() = 4;

    IBinder getService(String name) = 5;

    boolean registerClientBinder(IBinder iClientBinder) = 6;

    void unregisterClientBinder(IBinder iClientBinder) = 7;


}