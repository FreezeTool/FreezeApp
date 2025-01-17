package com.john.freezeapp.client;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

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
            if (isActive()) {
                iDaemonBinderContainerListener.bind(getDaemonBinderContainer());
            }
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
        iPackageManager.destroy();
        iActivityManager.destroy();
    }


    public final static Singleton<IPackageManager> iPackageManager = new Singleton<IPackageManager>() {
        @Override
        protected IPackageManager create() {
            if (!isActive()) {
                return null;
            }

            return IPackageManager.Stub.asInterface(new ClientSystemBinderWrapper(SystemServiceHelper.getSystemService("package")));
        }
    };

    public final static Singleton<IActivityManager> iActivityManager = new Singleton<IActivityManager>() {
        @Override
        protected IActivityManager create() {
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


}
