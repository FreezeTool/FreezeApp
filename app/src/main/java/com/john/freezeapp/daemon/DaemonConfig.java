package com.john.freezeapp.daemon;

import android.app.ActivityThread;
import android.os.Process;
import android.text.TextUtils;

import com.john.freezeapp.BuildConfig;

public class DaemonConfig {
    public static String getConfig(String key) {
        if (TextUtils.equals(key, DaemonHelper.KEY_DAEMON_VERSION)) {
            return BuildConfig.VERSION_NAME;
        } else if (TextUtils.equals(key, DaemonHelper.KEY_DAEMON_PACKAGE_NAME)) {
            String[] packagesNames;
            try {
                packagesNames = ActivityThread.getPackageManager().getPackagesForUid(Process.myUid());
                if (packagesNames != null && packagesNames.length > 0) {
                    return packagesNames[0];
                }
            } catch (Exception e) {
                //
            }
            return DaemonHelper.DAEMON_SHELL_PACKAGE;

        }
        return "";
    }
}
