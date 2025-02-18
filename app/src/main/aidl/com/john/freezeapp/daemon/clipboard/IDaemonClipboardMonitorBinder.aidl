package com.john.freezeapp.daemon.clipboard;
import com.john.freezeapp.daemon.clipboard.ClipboardData;
import com.john.freezeapp.daemon.clipboard.IDaemonClipboardChange;

interface IDaemonClipboardMonitorBinder {
    void startMonitor();
    void endMonitor();
    List<ClipboardData> getClipboardData();
    void removeClipboardData(String id);
    void clearClipboardData();
    boolean setClipboardData(String id);
    boolean isMonitor();
    void addClipboardDataChange(IDaemonClipboardChange iDaemonClipboardChange);
}