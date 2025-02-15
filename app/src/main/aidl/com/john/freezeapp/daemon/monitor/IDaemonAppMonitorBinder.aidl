// IDaemonAppMonitorBinder.aidl
package com.john.freezeapp.daemon.monitor;
// Declare any non-default types here with import statements
import com.john.freezeapp.daemon.monitor.DaemonAppMonitorConfig;
interface IDaemonAppMonitorBinder {
    boolean start();
    boolean stop();
    void updateSize(int size);
    int getSize();
    boolean isActive();
}