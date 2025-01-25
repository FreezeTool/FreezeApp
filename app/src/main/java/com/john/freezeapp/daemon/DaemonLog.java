package com.john.freezeapp.daemon;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DaemonLog {
    public static final String TAG = "freeze-server";

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(Throwable e, String msg) {
        Log.d(TAG, msg);
        Log.d(TAG, "----------------------------------------");
        Log.d(TAG, e.getMessage());
        Log.d(TAG, "----------------------------------------");
    }

    public static void toClient(int uid, int pid, String msg) {
        String date = sDateFormat.format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date);
        stringBuilder.append("    ");
        stringBuilder.append(uid);
        stringBuilder.append("    ");
        stringBuilder.append(pid);
        stringBuilder.append("    ");
        stringBuilder.append(TAG);
        stringBuilder.append(" : ");
        stringBuilder.append(msg);
        DaemonClientBinderProxy.notifyLog(stringBuilder.toString());
    }

    public static void toClient(String msg) {
        toClient(android.os.Process.myUid(), android.os.Process.myPid(), msg);
    }
}
