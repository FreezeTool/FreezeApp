package com.john.freezeapp.daemon;

import com.john.freezeapp.BuildConfig;

public class Custom {
    public static String getConfig(String key) {
        if (key.equals(DaemonHelper.KEY_DAEMON_VERSION)) {
            return BuildConfig.VERSION_NAME;
        }
        return "";
    }
}
