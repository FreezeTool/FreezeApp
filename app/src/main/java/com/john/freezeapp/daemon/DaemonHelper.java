package com.john.freezeapp.daemon;

import com.john.freezeapp.BuildConfig;

public class DaemonHelper {
    public static final int PORT = 33456;
    public static final String LOCALHOST = "localhost";

    public static final String IPC_TYPE_BIND = "bind";
    public static final String IPC_TYPE_STOP = "stop";
    public static final String IPC_TYPE_SHELL = "shell";
    public static final String IPC_TYPE_SERVICE = "service";
    public static final String ACTION_APP_PROCESS_START = "action.freeze.app.process.start";
    public static final String ACTION_APP_PROCESS_STOP = "action.freeze.app.process.stop";
    public static final String DAEMON_NICKNAME = "FreezeApp_" + BuildConfig.APPLICATION_ID;
    public static final String KEY_DAEMON_VERSION = "key_daemon_version";
    public static final String KEY_DAEMON_MODULE_CUSTOM = "FreezeApp";
    public static final String KEY_DAEMON_MODULE_SYSTEM = "System";
    public static final String KEY_DAEMON_MODULE_SYSTEM_PROPERTIES = "SystemProperties";

}
