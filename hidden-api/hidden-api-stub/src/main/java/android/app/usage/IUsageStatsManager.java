package android.app.usage;

import android.app.IActivityManager;
import android.app.PendingIntent;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IUsageStatsManager extends IInterface {
    ParceledListSlice queryUsageStats(int bucketType, long beginTime, long endTime,
                                      String callingPackage, int userId) throws RemoteException;

    ParceledListSlice queryConfigurationStats(int bucketType, long beginTime, long endTime,
                                              String callingPackage) throws RemoteException;

    ParceledListSlice queryEventStats(int bucketType, long beginTime, long endTime,
                                      String callingPackage) throws RemoteException;

    UsageEvents queryEvents(long beginTime, long endTime, String callingPackage) throws RemoteException;

    UsageEvents queryEventsForPackage(long beginTime, long endTime, String callingPackage) throws RemoteException;

    UsageEvents queryEventsForUser(long beginTime, long endTime, int userId, String callingPackage) throws RemoteException;

    UsageEvents queryEventsForPackageForUser(long beginTime, long endTime, int userId, String pkg, String callingPackage) throws RemoteException;

    void setAppInactive(String packageName, boolean inactive, int userId) throws RemoteException;

    boolean isAppInactive(String packageName, int userId, String callingPackage) throws RemoteException;

    void onCarrierPrivilegedAppsChanged();

    void reportChooserSelection(String packageName, int userId, String contentType, String[] annotations, String action) throws RemoteException;

    int getAppStandbyBucket(String packageName, String callingPackage, int userId) throws RemoteException;

    void setAppStandbyBucket(String packageName, int bucket, int userId) throws RemoteException;

    ParceledListSlice getAppStandbyBuckets(String callingPackage, int userId) throws RemoteException;

    void setAppStandbyBuckets(ParceledListSlice appBuckets, int userId) throws RemoteException;

    int getAppMinStandbyBucket(String packageName, String callingPackage, int userId) throws RemoteException;

    void setEstimatedLaunchTime(String packageName, long estimatedLaunchTime, int userId) throws RemoteException;

    void setEstimatedLaunchTimes(ParceledListSlice appLaunchTimes, int userId) throws RemoteException;

    void registerAppUsageObserver(int observerId, String[] packages, long timeLimitMs,
                                  PendingIntent callback, String callingPackage) throws RemoteException;

    void unregisterAppUsageObserver(int observerId, String callingPackage) throws RemoteException;

    void registerUsageSessionObserver(int sessionObserverId, String[] observed, long timeLimitMs,
                                      long sessionThresholdTimeMs, PendingIntent limitReachedCallbackIntent,
                                      PendingIntent sessionEndCallbackIntent, String callingPackage) throws RemoteException;

    void unregisterUsageSessionObserver(int sessionObserverId, String callingPackage) throws RemoteException;

    void registerAppUsageLimitObserver(int observerId, String[] packages, long timeLimitMs,
                                       long timeUsedMs, PendingIntent callback, String callingPackage) throws RemoteException;

    void unregisterAppUsageLimitObserver(int observerId, String callingPackage) throws RemoteException;

    void reportUsageStart(IBinder activity, String token, String callingPackage) throws RemoteException;

    void reportPastUsageStart(IBinder activity, String token, long timeAgoMs,
                              String callingPackage) throws RemoteException;

    void reportUsageStop(IBinder activity, String token, String callingPackage) throws RemoteException;

    void reportUserInteraction(String packageName, int userId) throws RemoteException;

    int getUsageSource() throws RemoteException;

    void forceUsageSourceSettingRead() throws RemoteException;

    long getLastTimeAnyComponentUsed(String packageName, String callingPackage) throws RemoteException;

    BroadcastResponseStatsList queryBroadcastResponseStats(
            String packageName, long id, String callingPackage, int userId) throws RemoteException;

    void clearBroadcastResponseStats(String packageName, long id, String callingPackage,
                                     int userId) throws RemoteException;

    void clearBroadcastEvents(String callingPackage, int userId) throws RemoteException;

    String getAppStandbyConstant(String key) throws RemoteException;


    abstract class Stub extends Binder implements IUsageStatsManager {

        public static IUsageStatsManager asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}
