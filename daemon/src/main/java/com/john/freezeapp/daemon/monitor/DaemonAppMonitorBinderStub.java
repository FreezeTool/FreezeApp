package com.john.freezeapp.daemon.monitor;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.ITaskStackListener;
import android.os.RemoteException;

import com.john.freezeapp.daemon.DaemonLog;
import com.john.freezeapp.daemon.DaemonService;
import com.john.freezeapp.monitor.TaskStackListener;

public class DaemonAppMonitorBinderStub extends IDaemonAppMonitorBinder.Stub {

    private int size;
    private ActivityManager.RunningTaskInfo runningTaskInfo;
    private boolean isActive;

    private ITaskStackListener.Stub sTaskStackListener = new TaskStackListener() {
        @Override
        public void onTaskMovedToFront(ActivityManager.RunningTaskInfo taskInfo) {
            super.onTaskMovedToFront(taskInfo);
            runningTaskInfo = taskInfo;
            updateRemoteTopActivity();
        }

        @Override
        public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo taskInfo) {
            super.onTaskDescriptionChanged(taskInfo);
            runningTaskInfo = taskInfo;
            updateRemoteTopActivity();
        }
    };

    private void updateRemoteTopActivity() {

    }

    @Override
    public boolean start() throws RemoteException {
        if (isActive) {
            return false;
        }
        try {
            IActivityManager activityManager = DaemonService.getActivityManager();
            if (activityManager != null) {
                if (sTaskStackListener != null) {
                    activityManager.unregisterTaskStackListener(sTaskStackListener);
                }

                activityManager.registerTaskStackListener(sTaskStackListener);
            }
            isActive = true;
            return true;
        } catch (Throwable e) {
            DaemonLog.e(e, "start");
        }
        return false;
    }

    @Override
    public boolean stop() throws RemoteException {
        if (!isActive) {
            return false;
        }
        try {
            IActivityManager activityManager = DaemonService.getActivityManager();
            if (activityManager != null) {
                activityManager.unregisterTaskStackListener(sTaskStackListener);
            }
            isActive = false;
            return true;
        } catch (Throwable e) {
            DaemonLog.e(e, "start");
        }
        return false;
    }

    @Override
    public void updateSize(int size) throws RemoteException {
        this.size = size;
        updateRemoteSize();
    }

    private void updateRemoteSize() {
        if (!this.isActive) {
            return;
        }
    }

    @Override
    public int getSize() throws RemoteException {
        return this.size;
    }

    @Override
    public boolean isActive() throws RemoteException {
        return this.isActive;
    }

    private void connectRemote() {

    }

}
