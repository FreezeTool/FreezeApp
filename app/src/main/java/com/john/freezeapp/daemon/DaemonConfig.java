package com.john.freezeapp.daemon;

import android.app.ActivityThread;
import android.app.smartspace.uitemplatedata.Text;
import android.os.Process;
import android.system.Os;
import android.text.TextUtils;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.daemon.util.DaemonUtil;
import com.john.freezeapp.util.UserHandleCompat;

public class DaemonConfig {
    public static String getConfig(String key) {
        if (TextUtils.equals(key, DaemonHelper.KEY_DAEMON_VERSION)) {
            return BuildConfig.VERSION_NAME;
        } else if (TextUtils.equals(key, DaemonHelper.KEY_DAEMON_PACKAGE_NAME)) {
            return DaemonUtil.getDaemonPackageName();
        } else if (TextUtils.equals(key, DaemonHelper.KEY_DAEMON_PID)) {
            return String.valueOf(android.os.Process.myPid());
        } else if (TextUtils.equals(key, DaemonHelper.KEY_DAEMON_UID)) {
            return String.valueOf(android.os.Process.myUid());
        } else if (TextUtils.equals(key, DaemonHelper.KEY_DAEMON_USERID)) {
            return String.valueOf(UserHandleCompat.myUserId());
        }
        return "";
    }
}
