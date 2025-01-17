// IDaemonBinderContainer.aidl
package com.john.freezeapp;

// Declare any non-default types here with import statements
import com.john.freezeapp.IRemoteProcess;
import android.os.IBinder;
interface IDaemonBinderContainer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getName() = 2;

    IRemoteProcess newProcess(in String[] cmd, in String[] env, in String dir) = 3;

    void closeDeamon() = 4;

    IBinder getService(String name) = 5;
}