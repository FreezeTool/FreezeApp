package com.john.freezeapp.daemon;

import android.app.ActivityManagerNative;
import android.app.ContentProviderHolder;
import android.app.IActivityManager;
import android.app.IActivityManagerPre26;
import android.app.IProcessObserver;
import android.content.AttributionSource;
import android.content.Context;
import android.content.IContentProvider;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.IDaemonBinderContainer;
import java.util.ArrayList;
import java.util.List;

public class DaemonBinder {

    private static final IDaemonBinderContainer sBinderContainer = new DaemonBinderContainer();
    private static final IProcessObserver.Stub iProcessObserverStub = new IProcessObserver.Stub() {

        private final List<Integer> PID_LIST = new ArrayList<>();

        @Override
        public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
            synchronized (PID_LIST) {
                if (PID_LIST.contains(pid) || !foregroundActivities) {
                    return;
                }
                PID_LIST.add(pid);
            }

            sendBinder(uid, pid);
        }

        @Override
        public void onProcessDied(int pid, int uid) throws RemoteException {
            synchronized (PID_LIST) {
                int index = PID_LIST.indexOf(pid);
                if (index != -1) {
                    PID_LIST.remove(index);
                }
            }
        }

        @Override
        public void onProcessStateChanged(int pid, int uid, int procState) throws RemoteException {
            synchronized (PID_LIST) {
                if (PID_LIST.contains(pid)) {
                    return;
                }
                PID_LIST.add(pid);
            }

            sendBinder(uid, pid);
        }

        @Override
        public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {

        }
    };

    public static void sendBinder(int uid, int pid) {
        try {
            if (isFreezeApp(uid)) {
                sendBinderContainer();
            }
        } catch (Throwable e) {
            DaemonLog.e(e, "isFreezeApp");
        }
    }

    public static void sendBinderContainer() {
        try {
            String name = "com.john.freezeapp.daemon";
            IContentProvider contentProvider = getContentProviderExternal(name, 0, null, name);
            if (contentProvider == null) {
                DaemonLog.log("sendBinder contentProvider = null");
                return;
            }
            Bundle extras = new Bundle();
            extras.putBinder("binder", sBinderContainer.asBinder());
            Bundle reply = callCompat(contentProvider, null, name, "sendBinder", null, extras);
        } catch (Throwable e) {
            DaemonLog.e(e, "sendBinderContainer");
        }
    }


    private static boolean isFreezeApp(int uid) throws RemoteException {
        String[] packages = getPackageManager().getPackagesForUid(uid);

        for (String aPackage : packages) {
            if (TextUtils.equals(aPackage, BuildConfig.APPLICATION_ID)) {
                return true;
            }
        }
        return false;
    }

    private static IPackageManager getPackageManager() {
        IBinder iBinder = ServiceManager.getService("package");
        return IPackageManager.Stub.asInterface(iBinder);
    }

    private static IActivityManager getActivityManager() {
        IBinder iBinder = ServiceManager.getService(Context.ACTIVITY_SERVICE);
        IActivityManager am;
        if (Build.VERSION.SDK_INT >= 26) {
            am = IActivityManager.Stub.asInterface(iBinder);
        } else {
            am = ActivityManagerNative.asInterface(iBinder);
        }
        return am;
    }

    public static IContentProvider getContentProviderExternal(@Nullable String name, int userId, @Nullable IBinder token, @Nullable String tag) throws RemoteException {
        IActivityManager am = DaemonService.activityManager;
        ContentProviderHolder contentProviderHolder;
        IContentProvider provider;
        if (Build.VERSION.SDK_INT >= 29) {
            contentProviderHolder = am.getContentProviderExternal(name, userId, token, tag);
            provider = contentProviderHolder != null ? contentProviderHolder.provider : null;
            DaemonLog.log("contentProviderHolder=" + contentProviderHolder);
        } else if (Build.VERSION.SDK_INT >= 26) {
            contentProviderHolder = am.getContentProviderExternal(name, userId, token);
            provider = contentProviderHolder != null ? contentProviderHolder.provider : null;
        } else {
            provider = Refine.<IActivityManagerPre26>unsafeCast(am).getContentProviderExternal(name, userId, token).provider;
        }

        return provider;
    }


    public static Bundle callCompat(@NonNull IContentProvider provider, @Nullable String callingPkg, @Nullable String authority, @Nullable String method, @Nullable String arg, @Nullable Bundle extras) throws RemoteException {
        Bundle result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result = provider.call((new AttributionSource.Builder(android.system.Os.getuid())).setPackageName(callingPkg).build(), authority, method, arg, extras);
        } else if (Build.VERSION.SDK_INT >= 30) {
            result = provider.call(callingPkg, (String) null, authority, method, arg, extras);
        } else if (Build.VERSION.SDK_INT >= 29) {
            result = provider.call(callingPkg, authority, method, arg, extras);
        } else {
            result = provider.call(callingPkg, method, arg, extras);
        }

        return result;
    }


    private static Context sContext;

    public static void register(Context context) {
        sContext = context;
        try {
            getActivityManager().registerProcessObserver(iProcessObserverStub);
        } catch (Throwable e) {
            //
        }
    }

    public static void unregister() {
        try {
            getActivityManager().unregisterProcessObserver(iProcessObserverStub);
        } catch (Throwable e) {
            //
        }
    }
}
