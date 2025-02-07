// IDaemonAppMonitorBinder.aidl
package com.john.freezeapp.daemon.monitor;
// Declare any non-default types here with import statements
import com.john.freezeapp.daemon.monitor.DaemonAppMonitorConfig;
interface IDaemonAppMonitorBinder {
    void start();
    void stop();
    void update(in DaemonAppMonitorConfig config);
}