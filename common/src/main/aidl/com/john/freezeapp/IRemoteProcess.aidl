// IRemoteProcess.aidl
package com.john.freezeapp;

// Declare any non-default types here with import statements

interface IRemoteProcess {
    ParcelFileDescriptor getOutputStream();

    ParcelFileDescriptor getInputStream();

    ParcelFileDescriptor getErrorStream();

    int waitFor();

    int exitValue();

    void destroy();

    boolean alive();

    boolean waitForTimeout(long timeout, String unit);
}