package com.john.freezeapp.daemon.clipboard;
import com.john.freezeapp.daemon.clipboard.ClipboardData;

interface IDaemonClipboardMonitorBinder {
    void startMonitor();
    void endMonitor();
    List<ClipboardData> getClipboardData();
    void removeClipboardData(String id);
    void clearClipboardData();
    boolean setClipboardData(String id);
    boolean isMonitor();
}