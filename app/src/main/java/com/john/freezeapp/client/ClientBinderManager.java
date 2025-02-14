package com.john.freezeapp.client;

import android.annotation.TargetApi;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.smartspace.ISmartspaceManager;
import android.app.usage.IStorageStatsManager;
import android.app.usage.IUsageStatsManager;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.IBinderHidden;
import android.os.IInstalld;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.storage.IStorageManager;
import android.permission.IPermissionManager;
import android.view.IWindowManager;

import com.android.internal.app.IAppOpsService;
import com.android.internal.app.IBatteryStats;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.client.process.ClientRemoteProcess;
import com.john.hidden.api.ReplaceRef;

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
        GlobalServiceManager.onDaemonBind(binderContainer);
    }

    private synchronized static void deathDaemonBinder(IDaemonBinder binderContainer) {
        ClientLog.log("deathDaemonBinder sDaemonBinder=" + sDaemonBinder + ",binderContainer=" + binderContainer);
        if (sDaemonBinder == binderContainer) {
            sDaemonBinder = null;
            notifyUnbindDaemonBinderListener();
            GlobalServiceManager.onDaemonUnbind();
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

    /**
     * 执行系统服务的dump函数
     * @param serviceName 系统服务注册的名字，这里是通过Daemon代理帮忙调用
     * @param fd
     * @param args
     * @throws RemoteException
     */
    public static void dump(String serviceName, FileDescriptor fd, String[] args) throws RemoteException {
        if (args == null) {
            args = new String[0];
        }
        String[] dumpArgs = new String[args.length + 1];
        dumpArgs[0] = serviceName;
        System.arraycopy(args, 0, dumpArgs, 1, args.length);
        getDaemonBinder().asBinder().dump(fd, dumpArgs);
    }

    /**
     * 执行系统服务的shellCommand函数，这里是通过Daemon代理帮忙调用
     * @param serviceName 系统服务注册的名字
     * @param in
     * @param out
     * @param err
     * @param args
     * @param shellCallback
     * @param resultReceiver
     * @throws Exception
     */
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
        ReplaceRef.<IBinderHidden>unsafeCast(getDaemonBinder().asBinder()).shellCommand(in, out, err, args, shellCallback, resultReceiver);
    }
}
