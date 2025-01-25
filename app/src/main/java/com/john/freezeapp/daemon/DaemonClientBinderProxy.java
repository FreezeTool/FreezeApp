package com.john.freezeapp.daemon;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.john.freezeapp.IClientBinder;
import com.john.freezeapp.IClientLogBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DaemonClientBinderProxy {

    public static final Map<Integer, Map<String, IInterface>> sClientBinderMap = new ConcurrentHashMap<>();


    private static String getInterfaceDescriptor(IBinder iClientBinder) {
        try {
            return iClientBinder.getInterfaceDescriptor();
        } catch (RemoteException e) {
            return null;
        }
    }

    public static void registerClientBinder(int uid, int pid, IBinder iClientBinder) {

        fixClientBinderMap();

        String descriptor = getInterfaceDescriptor(iClientBinder);

        if (descriptor == null) {
            return;
        }
        DaemonLog.toClient(uid, pid, "call registerClientBinder descriptor=" + descriptor);
        addClientBinder(pid, descriptor, iClientBinder);

        printClientBinder("registerClientBinder");
    }

    private static void printClientBinder(String tag) {
        DaemonLog.log("------------------" + tag + "------------------");
        for (Map.Entry<Integer, Map<String, IInterface>> entry : sClientBinderMap.entrySet()) {
            DaemonLog.log("pid = " + entry.getKey());
            for (Map.Entry<String, IInterface> binderEntry : entry.getValue().entrySet()) {
                DaemonLog.log("client = " + binderEntry.getKey());
            }
        }
        DaemonLog.log("-----------------------------------------------------");
    }

    private static void fixClientBinderMap() {
        for (Map.Entry<Integer, Map<String, IInterface>> entry : sClientBinderMap.entrySet()) {
            entry.getValue().values().removeIf(next -> !next.asBinder().isBinderAlive());
        }
        sClientBinderMap.values().removeIf(Map::isEmpty);
    }

    private static void addClientBinder(int pid, String descriptor, IBinder iClientBinder) {
        IInterface binderProxy = null;
        switch (descriptor) {
            case IClientBinder.DESCRIPTOR:
                binderProxy = IClientBinder.Stub.asInterface(iClientBinder);
                break;
            case IClientLogBinder.DESCRIPTOR:
                binderProxy = IClientLogBinder.Stub.asInterface(iClientBinder);
                break;
        }

        if (binderProxy != null) {
            Map<String, IInterface> binderMap = sClientBinderMap.get(pid);
            if (binderMap == null) {
                binderMap = new ConcurrentHashMap<>();
                sClientBinderMap.put(pid, binderMap);
            }
            try {
                iClientBinder.linkToDeath(() -> {
                    DaemonLog.toClient("linkToDeath pid= " + pid + ", descriptor=" + descriptor);
                    removeClientBinder(pid, descriptor);
                }, 0);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            binderMap.put(descriptor, binderProxy);
        }
    }

    private static void removeClientBinder(int callingPid, String descriptor) {
        Map<String, IInterface> binderMap = sClientBinderMap.get(callingPid);
        if (binderMap != null) {
            binderMap.remove(descriptor);
        }
    }

    public static void unregisterClientBinder(int uid, int pid, IBinder iClientBinder) {
        if (iClientBinder.isBinderAlive()) {
            String descriptor = getInterfaceDescriptor(iClientBinder);
            DaemonLog.toClient(uid, pid, "call registerClientBinder descriptor=" + descriptor);
            if (descriptor != null) {
                removeClientBinder(pid, descriptor);
            }
        }
        printClientBinder("unregisterClientBinder");
    }

    public static <T> List<T> getClientBinder(String descriptor) {
        List<T> list = new ArrayList<>();
        for (Map<String, IInterface> value : sClientBinderMap.values()) {
            IInterface iInterface = value.get(descriptor);
            if (iInterface != null) {
                list.add((T) iInterface);
            }
        }
        return list;
    }

    public static void notifyLog(String msg) {
        List<IClientLogBinder> clientBinder = getClientBinder(IClientLogBinder.DESCRIPTOR);
        for (IClientLogBinder iClientLogBinder : clientBinder) {
            try {
                iClientLogBinder.log(msg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
