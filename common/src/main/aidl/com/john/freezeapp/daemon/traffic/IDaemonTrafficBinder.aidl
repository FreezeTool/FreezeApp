// IRunAsBinder.aidl
package com.john.freezeapp.daemon.traffic;
// Declare any non-default types here with import statements

interface IDaemonTrafficBinder {

    void start(int threshold, int matchRule);
    void stop();
    boolean isActive();
}