package com.john.freezeapp.client;

import com.john.freezeapp.daemon.DaemonHelper;

public class ClientDaemonService {
    public static String getDaemonPackageName() {
        return ClientBinderManager.getConfig(DaemonHelper.DAEMON_MODULE_CUSTOM, DaemonHelper.KEY_DAEMON_PACKAGE_NAME);
    }

    public static int getDaemonUid() {
        String uid = ClientBinderManager.getConfig(DaemonHelper.DAEMON_MODULE_CUSTOM, DaemonHelper.KEY_DAEMON_UID);
        try {
            return Integer.parseInt(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDaemonPid() {

        String pid = ClientBinderManager.getConfig(DaemonHelper.DAEMON_MODULE_CUSTOM, DaemonHelper.KEY_DAEMON_PID);
        try {
            return Integer.parseInt(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDaemonUserId() {

        String userId = ClientBinderManager.getConfig(DaemonHelper.DAEMON_MODULE_CUSTOM, DaemonHelper.KEY_DAEMON_USERID);
        try {
            return Integer.parseInt(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
