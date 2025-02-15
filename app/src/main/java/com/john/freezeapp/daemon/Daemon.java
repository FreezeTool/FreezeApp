package com.john.freezeapp.daemon;

import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;

import androidx.annotation.Keep;

@Keep
public class Daemon {


    private Handler mHandler = null;
    public ActivityThread mActivityThread = null;

    private static Daemon sDaemon;


    public static void main(String[] args) {
        DaemonLog.log("Daemon start");
        Looper.prepareMainLooper();
        sDaemon = new Daemon();
        Looper.loop();
        DaemonLog.toClient("Daemon close");
        DaemonLog.log("Daemon finish");
        System.exit(0);
    }


    public static Daemon getDaemon() {
        return sDaemon;
    }

    public Application getApplication() {
        return mActivityThread.getApplication();
    }

    public Context getContext() {
        return mActivityThread.getSystemContext();
    }

    private void killOtherDaemon() {
        DaemonShellUtils.execCommand(String.format("ps | grep '%s' | awk '{print $2}'", DaemonHelper.DAEMON_NICKNAME), false, new DaemonShellUtils.ShellCommandResultCallback() {
            @Override
            public void callback(DaemonShellUtils.ShellCommandResult commandResult) {
                int cPid = Process.myPid();
                DaemonLog.log(commandResult.toString());
                if (commandResult.result && !TextUtils.isEmpty(commandResult.successMsg)) {
                    String[] pids = commandResult.successMsg.split("\\s+");
                    for (String pid : pids) {
                        if (!TextUtils.equals(pid, cPid + "")) {
//                            android.os.Process.killProcess(pid);
                            DaemonShellUtils.execCommand("kill -9 " + pid, false, null);
                        }
                    }
                }
            }
        });
    }

    public Daemon() {
        killOtherDaemon();
        mHandler = new Handler(Looper.getMainLooper());
        mActivityThread = ActivityThread.systemMain();
        DaemonBinderManager.register(mActivityThread.getSystemContext());
        DaemonBinderManager.sendBinderContainer();

        try {
            String[] packageNames = ActivityThread.getPackageManager().getPackagesForUid(
                    Process.myUid());
            if (packageNames != null) {
                for (String packageName : packageNames) {
                    DaemonLog.log("packageName - " + packageName);
                }
            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }


    public Handler getHandler() {
        return mHandler;
    }

    public void stop() {
        System.exit(0);
    }

}
