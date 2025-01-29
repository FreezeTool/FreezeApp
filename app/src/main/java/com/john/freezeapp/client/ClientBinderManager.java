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
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.view.IWindowManager;

import com.android.internal.app.IBatteryStats;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.client.process.ClientRemoteProcess;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ClientBinderManager {
    public static String getConfig(String module, String key) {
        try {
            return getDaemonBinder().getConfig(module, key);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface IDaemonBinderListener {
        void bind(IDaemonBinder daemonBinder);

        void unbind();
    }

    private static IDaemonBinder sDaemonBinder;


    private static final List<IDaemonBinderListener> sDaemonBinderListeners = new ArrayList<>();

    public static void registerDaemonBinderListener(IDaemonBinderListener iDaemonBinderListener) {
        if (!sDaemonBinderListeners.contains(iDaemonBinderListener)) {
            sDaemonBinderListeners.add(iDaemonBinderListener);
        }
    }

    public static void unregisterDaemonBinderListener(IDaemonBinderListener iDaemonBinderListener) {
        sDaemonBinderListeners.remove(iDaemonBinderListener);
    }

    private static void notifyBindDaemonBinderListener(IDaemonBinder binderContainer) {
        for (IDaemonBinderListener sDaemonBinderListener : sDaemonBinderListeners) {
            sDaemonBinderListener.bind(binderContainer);
        }
    }

    private static void notifyUnbindDaemonBinderListener() {
        for (IDaemonBinderListener sDaemonBinderListener : sDaemonBinderListeners) {
            sDaemonBinderListener.unbind();
        }
    }

    static synchronized void setDaemonBinder(IDaemonBinder binderContainer) {
        ClientLog.log("setDaemonBinder=" + binderContainer);
        sDaemonBinder = binderContainer;
        notifyBindDaemonBinderListener(binderContainer);
        try {
            binderContainer.asBinder().linkToDeath(() -> {
                deathDaemonBinder(binderContainer);
            }, 0);
        } catch (Throwable e) {
            ClientLog.e(e, "ClientBinder binderDied");
        }
    }

    private synchronized static void deathDaemonBinder(IDaemonBinder binderContainer) {
        ClientLog.log("deathDaemonBinder sDaemonBinder=" + sDaemonBinder + ",binderContainer=" + binderContainer);
        if (sDaemonBinder == binderContainer) {
            sDaemonBinder = null;
            notifyUnbindDaemonBinderListener();
        }
    }


    public static boolean isActive() {
        return sDaemonBinder != null;
    }

    public static IDaemonBinder getDaemonBinder() {
        return sDaemonBinder;
    }


    public static void registerClientBinder(IBinder iBinder) {
        if (isActive()) {
            try {
                sDaemonBinder.registerClientBinder(iBinder);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void unregisterClientBinder(IBinder iBinder) {
        if (isActive()) {
            try {
                sDaemonBinder.unregisterClientBinder(iBinder);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
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
        return new ClientRemoteProcess(getDaemonBinder().newProcess(cmdarray, envp, dir));
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
            if (FreezeUtil.atLeast26()) {
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
            try {
                return IBatteryStats.Stub.asInterface(new ClientSystemBinderWrapper(SystemServiceHelper.getSystemService("batterystats")));
            } catch (Throwable e) {
                return null;
            }
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

    private final static ClientBinderSingleton<IWindowManager> iWindowManager = new ClientBinderSingleton<IWindowManager>() {
        @Override
        protected IWindowManager createBinder() {

            if (!isActive()) {
                return null;
            }

            IBinder systemService = SystemServiceHelper.getSystemService("window");
            if (systemService != null) {
                return IWindowManager.Stub.asInterface(new ClientSystemBinderWrapper(systemService));
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

    public static IWindowManager getWindowManager() {
        return iWindowManager.get();
    }

    public static void dump(String serviceName, FileDescriptor fd, String[] args) throws RemoteException {
        if (args == null) {
            args = new String[0];
        }
        String[] dumpArgs = new String[args.length + 1];
        dumpArgs[0] = serviceName;
        System.arraycopy(args, 0, dumpArgs, 1, args.length);
        getDaemonBinder().asBinder().dump(fd, dumpArgs);
    }


    public static void shellCommand(String serviceName, FileDescriptor in, FileDescriptor out,
                                    FileDescriptor err,
                                    String[] args, ShellCallback shellCallback,
                                    ResultReceiver resultReceiver) throws Exception {
        if (args == null) {
            args = new String[0];
        }
        String[] dumpArgs = new String[args.length + 1];
        dumpArgs[0] = serviceName;
        System.arraycopy(args, 0, dumpArgs, 1, args.length);
        FreezeUtil.shellCommand(getDaemonBinder().asBinder(), in, out, err, dumpArgs, shellCallback, resultReceiver);
    }


}
