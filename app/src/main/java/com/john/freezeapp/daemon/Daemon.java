package com.john.freezeapp.daemon;

import android.app.ActivityThread;
import android.app.IActivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import androidx.annotation.Keep;

import java.lang.reflect.Method;

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
        DaemonLog.log("Daemon finish");
    }


    public static Daemon getDaemon() {
        return sDaemon;
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
//        new DaemonSocketServer(Daemon.this);
        DaemonBinderManager.register(mActivityThread.getSystemContext());
        DaemonBinderManager.sendBinderContainer();
        test();
    }

    private void test() {
        Method[] declaredMethods = IActivityManager.Stub.class.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            DaemonLog.log(declaredMethod.getName());
        }
    }


    public void stop() {
        System.exit(0);
//        DaemonShellUtils.stop();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Looper.getMainLooper().quit();
//            }
//        });
    }

}
