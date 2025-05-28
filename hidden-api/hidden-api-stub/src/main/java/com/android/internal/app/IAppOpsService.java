package com.android.internal.app;

import android.app.AppOpsManagerHidden;
import android.app.AsyncNotedAppOp;
import android.app.SyncNotedAppOp;
import android.content.AttributionSource;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallback;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import java.util.List;

public interface IAppOpsService extends IInterface {

    void resetAllModes(int userId, String packageName)
            throws RemoteException;

    void setMode(int code, int uid, String packageName, int mode)
            throws RemoteException;

    void setUidMode(int code, int uid, int mode)
            throws RemoteException;

    List<AppOpsManagerHidden.PackageOps> getOpsForPackage(int uid, String packageName, int[] ops)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.O)
    List<AppOpsManagerHidden.PackageOps> getUidOps(int uid, int[] ops)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.P)
    void startWatchingActive(int[] ops, IAppOpsActiveCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.P)
    void stopWatchingActive(IAppOpsActiveCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.P)
    boolean isOperationActive(int code, int uid, String packageName)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    void startWatchingNoted(int[] ops, IAppOpsNotedCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    void stopWatchingNoted(IAppOpsNotedCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    void getHistoricalOps(int uid, String packageName, List<String> ops, long beginTimeMillis,
                          long endTimeMillis, int flags, RemoteCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.R)
    void getHistoricalOps(int uid, String packageName, String attributionTag, List<String> ops, int filter,
                          long beginTimeMillis, long endTimeMillis, int flags, RemoteCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.S)
    void getHistoricalOps(int uid, String packageName, String attributionTag, List<String> ops,
                          int historyFlags, int filter, long beginTimeMillis, long endTimeMillis, int flags,
                          RemoteCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    void getHistoricalOpsFromDiskRaw(int uid, String packageName, List<String> ops, long beginTimeMillis,
                                     long endTimeMillis, int flags, RemoteCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.R)
    void getHistoricalOpsFromDiskRaw(int uid, String packageName, String attributionTag, List<String> ops, int filter,
                                     long beginTimeMillis, long endTimeMillis, int flags, RemoteCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.S)
    void getHistoricalOpsFromDiskRaw(int uid, String packageName, String attributionTag,
                                     List<String> ops, int historyFlags, int filter, long beginTimeMillis,
                                     long endTimeMillis, int flags, RemoteCallback callback)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    void setHistoryParameters(int mode, long baseSnapshotInterval, int compressionStep)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    void resetHistoryParameters()
            throws RemoteException;


    int checkOperation(int code, int uid, String packageName);

    SyncNotedAppOp noteOperation(int code, int uid, String packageName, String attributionTag,
                                 boolean shouldCollectAsyncNotedOp, String message, boolean shouldCollectMessage);

    SyncNotedAppOp startOperation(IBinder clientId, int code, int uid, String packageName,
                                  String attributionTag, boolean startIfModeDefault,
                                  boolean shouldCollectAsyncNotedOp, String message, boolean shouldCollectMessage,
                                  int attributionFlags, int attributionChainId);

    void finishOperation(IBinder clientId, int code, int uid, String packageName,
                         String attributionTag);

    void startWatchingMode(int op, String packageName, IAppOpsCallback callback);

    void stopWatchingMode(IAppOpsCallback callback);

    int permissionToOpCode(String permission);

    int checkAudioOperation(int code, int usage, int uid, String packageName);

    boolean shouldCollectNotes(int opCode);

    void setCameraAudioRestriction(int mode);
    // End of methods also called by native code.
    // Any new method exposed to native must be added after the last one, do not reorder

    SyncNotedAppOp noteProxyOperation(int code, AttributionSource attributionSource,
                                      boolean shouldCollectAsyncNotedOp, String message, boolean shouldCollectMessage,
                                      boolean skipProxyOperation);

    SyncNotedAppOp startProxyOperation(IBinder clientId, int code,
                                       AttributionSource attributionSource, boolean startIfModeDefault,
                                       boolean shouldCollectAsyncNotedOp, String message, boolean shouldCollectMessage,
                                       boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlags,
                                       int attributionChainId);

    void finishProxyOperation(IBinder clientId, int code, AttributionSource attributionSource,
                              boolean skipProxyOperation);


    int checkPackage(int uid, String packageName);

//    RuntimeAppOpAccessMessage collectRuntimeAppOpAccessMessage();
//    MessageSamplingConfig reportRuntimeAppOpAccessMessageAndGetConfig(String packageName,
//                                                                      SyncNotedAppOp appOp, String message);

    List<AppOpsManagerHidden.PackageOps> getPackagesForOps(int[] ops);


    void offsetHistory(long duration);

//    void addHistoricalOps(AppOpsManager.HistoricalOps ops);

    void resetPackageOpsNoHistory(String packageName);

    void clearHistory();

    void rebootHistory(long offlineDurationMillis);

    void setAudioRestriction(int code, int usage, int uid, int mode, String[] exceptionPackages);

    void setUserRestrictions(Bundle restrictions, IBinder token, int userHandle);

//    void setUserRestriction(int code, boolean restricted, IBinder token, int userHandle, PackageTagsList excludedPackageTags);

    void removeUser(int userHandle);


    boolean isProxying(int op, String proxyPackageName, String proxyAttributionTag, int proxiedUid,
                       String proxiedPackageName);

    void startWatchingStarted(int[] ops, IAppOpsStartedCallback callback);

    void stopWatchingStarted(IAppOpsStartedCallback callback);

    void startWatchingModeWithFlags(int op, String packageName, int flags, IAppOpsCallback callback);

    void startWatchingAsyncNoted(String packageName, IAppOpsAsyncNotedCallback callback);

    void stopWatchingAsyncNoted(String packageName, IAppOpsAsyncNotedCallback callback);

    List<AsyncNotedAppOp> extractAsyncOps(String packageName);

    int checkOperationRaw(int code, int uid, String packageName, String attributionTag);

    void reloadNonHistoricalState();

    void collectNoteOpCallsForValidation(String stackTrace, int op, String packageName, long version);


    abstract class Stub implements IAppOpsService {

        public static IAppOpsService asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}
