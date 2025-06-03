package com.john.freezeapp.traffic;

import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.os.IBinder;
import android.os.RemoteException;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.traffic.IDaemonTrafficBinder;
import com.john.freezeapp.daemon.traffic.TrafficConstant;
import com.john.freezeapp.util.SharedPrefUtil;

public class ClientTrafficMonitor {

    private static IDaemonTrafficBinder getDaemonTrafficBinder() {
        try {
            IBinder service = ClientBinderManager.getDaemonBinder().getService(DaemonHelper.DAEMON_BINDER_TRAFFIC_MONITOR);
            if (service != null) {
                return IDaemonTrafficBinder.Stub.asInterface(service);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void start(int threshold, int matchRule) {
        IDaemonTrafficBinder daemonTrafficBinder = getDaemonTrafficBinder();
        if (daemonTrafficBinder != null) {
            try {
                daemonTrafficBinder.start(threshold, matchRule);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void stop() {
        IDaemonTrafficBinder daemonTrafficBinder = getDaemonTrafficBinder();
        if (daemonTrafficBinder != null) {
            try {
                daemonTrafficBinder.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getHistory() {
        INetworkStatsService networkStatsService = ClientSystemService.getNetworkStatsService();
        INetworkStatsSession iNetworkStatsSession = networkStatsService.openSessionForUsageStats(0, BuildConfig.APPLICATION_ID);
        if (iNetworkStatsSession != null) {
//            iNetworkStatsSession.getHistoryForNetwork()
        }
    }

    public static int getTrafficThreshold() {
        return SharedPrefUtil.getInt(SharedPrefUtil.KEY_TRAFFIC_THRESHOLD, 0);
    }

    public static void setTrafficThreshold(int threshold) {
        SharedPrefUtil.setInt(SharedPrefUtil.KEY_TRAFFIC_THRESHOLD, threshold);
    }


    public static int getTrafficType() {
        return SharedPrefUtil.getInt(SharedPrefUtil.KEY_TRAFFIC_TYPE, TrafficConstant.TRAFFIC_MOBILE);
    }

    public static void setTrafficType(int matchRule) {
        SharedPrefUtil.setInt(SharedPrefUtil.KEY_TRAFFIC_TYPE, matchRule);
    }


    public static boolean isTrafficSwitchOpen() {
        return SharedPrefUtil.getBoolean(SharedPrefUtil.KEY_TRAFFIC_SWITCHER, false);
    }

    public static void setTrafficSwitch(boolean isOpen) {
        SharedPrefUtil.setBoolean(SharedPrefUtil.KEY_TRAFFIC_SWITCHER, isOpen);
    }

    public static boolean isActive() {
        IDaemonTrafficBinder daemonTrafficBinder = getDaemonTrafficBinder();
        if (daemonTrafficBinder != null) {
            try {
                return daemonTrafficBinder.isActive();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
