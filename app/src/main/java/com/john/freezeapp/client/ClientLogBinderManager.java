package com.john.freezeapp.client;

import java.util.ArrayList;
import java.util.List;

public class ClientLogBinderManager {

    public static ClientLogBinderStub clientLogBinder = new ClientLogBinderStub();

    public static class LogData {
        public String msg;

        public LogData(String msg) {
            this.msg = msg;
        }
    }

    private final static List<LogData> logDatas = new ArrayList<>();

    public synchronized static List<LogData> getLogData() {
        return logDatas;
    }

    public synchronized static void unbindDaemon() {
        logDatas.clear();
    }

    static synchronized void notifyLog(String msg) {
        logDatas.add(new LogData(msg));
        for (ClientLogCallback clientLogCallback : clientLogCallbacks) {
            clientLogCallback.log(msg);
        }
    }

    public interface ClientLogCallback {
        void log(String msg);
    }

    private static final List<ClientLogCallback> clientLogCallbacks = new ArrayList<>();

    public static void registerClientLogCallback(ClientLogCallback callback) {
        if (!clientLogCallbacks.contains(callback)) {
            clientLogCallbacks.add(callback);
        }
    }

    public static void unregisterClientLogCallback(ClientLogCallback callback) {
        clientLogCallbacks.remove(callback);
    }


    public static void registerClientLogBinder() {
        if (ClientBinderManager.isActive()) {
            ClientBinderManager.registerClientBinder(clientLogBinder);
        }
    }

    public static void unregisterClientLogBinder() {
        if (ClientBinderManager.isActive()) {
            ClientBinderManager.registerClientBinder(clientLogBinder);
        }
    }


}
