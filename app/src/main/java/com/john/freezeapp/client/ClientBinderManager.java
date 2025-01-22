package com.john.freezeapp.client;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.smartspace.ISmartspaceManager;
import android.app.usage.IUsageStatsManager;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.internal.app.IBatteryStats;
import com.john.freezeapp.IDaemonBinderContainer;
import com.john.freezeapp.Singleton;
import com.john.freezeapp.client.process.ClientRemoteProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ClientBinderManager {
    public interface IDaemonBinderContainerListener {
        void bind(IDaemonBinderContainer daemonBinderContainer);

        void unbind();
    }

    private static IDaemonBinderContainer sDaemonBinderContainer;


    private static final List<IDaemonBinderContainerListener> sDaemonBinderContainerListeners = new ArrayList<>();

    public static void registerDaemonBinderContainerListener(IDaemonBinderContainerListener iDaemonBinderContainerListener) {
        if (!sDaemonBinderContainerListeners.contains(iDaemonBinderContainerListener)) {
            sDaemonBinderContainerListeners.add(iDaemonBinderContainerListener);
        }
    }

    public static void unregisterDaemonBinderContainerListener(IDaemonBinderContainerListener iDaemonBinderContainerListener) {
        sDaemonBinderContainerListeners.remove(iDaemonBinderContainerListener);
    }

    private static void notifyBindDaemonBinderContainerListener(IDaemonBinderContainer binderContainer) {
        for (IDaemonBinderContainerListener sDaemonBinderContainerListener : sDaemonBinderContainerListeners) {
            sDaemonBinderContainerListener.bind(binderContainer);
        }
    }

    private static void notifyUnbindDaemonBinderContainerListener() {
        for (IDaemonBinderContainerListener sDaemonBinderContainerListener : sDaemonBinderContainerListeners) {
            sDaemonBinderContainerListener.unbind();
        }
    }

    static synchronized void setDaemonBinderContainer(IDaemonBinderContainer binderContainer) {
        ClientLog.log("setDaemonBinderContainer=" + binderContainer);
        sDaemonBinderContainer = binderContainer;
        notifyBindDaemonBinderContainerListener(binderContainer);
        try {
            binderContainer.asBinder().linkToDeath(() -> {
                deathDaemonBinderContainer(binderContainer);
            }, 0);
        } catch (Throwable e) {
            ClientLog.e(e, "ClientBinder binderDied");
        }
    }

    private synchronized static void deathDaemonBinderContainer(IDaemonBinderContainer binderContainer) {
        ClientLog.log("deathDaemonBinderContainer sDaemonBinderContainer=" + sDaemonBinderContainer + ",binderContainer=" + binderContainer);
        if (sDaemonBinderContainer == binderContainer) {
            sDaemonBinderContainer = null;
            notifyUnbindDaemonBinderContainerListener();
        }
        deathServerBinder();
    }


    public static boolean isActive() {
        return sDaemonBinderContainer != null;
    }

    public static IDaemonBinderContainer getDaemonBinderContainer() {
        return sDaemonBinderContainer;
    }


    public static ClientRemoteProcess exec(String command) throws RemoteException {
        return exec(command, null, null);
    }

    public static ClientRemoteProcess exec(String command, String[] envp, String dir)
            throws RemoteException {
        if (command.isEmpty())
            throw new IllegalArgumentException("Empty command");

        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
            cmdarray[i] = st.nextToken();
        return new ClientRemoteProcess(getDaemonBinderContainer().newProcess(cmdarray, envp, dir));
    }

    private static void deathServerBinder() {

    }


    private final static ClientBinderSingleton<IPackageManager> iPackageManager = new ClientBinderSingleton<IPackageManager>() {
        @Override
        protected IPackageManager createBinder() {
            if (!isActive()) {
                return null;
            }

            return IPackageManager.Stub.asInterface(new ClientSystemBinderWrapper(SystemServiceHelper.getSystemService("package")));
        }
    };

    private final static ClientBinderSingleton<IActivityManager> iActivityManager = new ClientBinderSingleton<IActivityManager>() {
        @Override
        protected IActivityManager createBinder() {
            if (!isActive()) {
                return null;
            }
            IBinder binder = new ClientSystemBinderWrapper(SystemServiceHelper.getSystemService(Context.ACTIVITY_SERVICE));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return IActivityManager.Stub.asInterface(binder);
            } else {
                return ActivityManagerNative.asInterface(binder);
            }
        }
    };


    private final static ClientBinderSingleton<IBatteryStats> iBatteryStats = new ClientBinderSingleton<IBatteryStats>() {
        @Override
        protected IBatteryStats createBinder() {
            if (!isActive()) {
                return null;
            }
            return IBatteryStats.Stub.asInterface(new ClientSystemBinderWrapper(SystemServiceHelper.getSystemService("batterystats")));
        }
    };

    private final static ClientBinderSingleton<IUsageStatsManager> iUsageStatsManager = new ClientBinderSingleton<IUsageStatsManager>() {
        @Override
        protected IUsageStatsManager createBinder() {
            if (!isActive()) {
                return null;
            }
            return IUsageStatsManager.Stub.asInterface(new ClientSystemBinderWrapper(SystemServiceHelper.getSystemService(Context.USAGE_STATS_SERVICE)));
        }
    };


    private final static ClientBinderSingleton<ISmartspaceManager> iSmartspaceManager = new ClientBinderSingleton<ISmartspaceManager>() {
        @Override
        protected ISmartspaceManager createBinder() {

            IBinder systemService = SystemServiceHelper.getSystemService("smartspace");
            if (systemService != null) {
                return ISmartspaceManager.Stub.asInterface(new ClientSystemBinderWrapper(systemService));
            }
            return null;
        }
    };


    public static IActivityManager getActivityManager() {
        return iActivityManager.get();
    }

    public static IPackageManager getPackageManager() {
        return iPackageManager.get();
    }

    public static IBatteryStats getBatteryStats() {
        return iBatteryStats.get();
    }

    public static IUsageStatsManager getUsageStatsManager() {
        return iUsageStatsManager.get();
    }

    public static ISmartspaceManager getSmartspaceManager() {
        return iSmartspaceManager.get();
    }


}
