package android.app.usage;

import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;

public interface IStorageStatsManager {
    boolean isQuotaSupported(String volumeUuid, String callingPackage);

    boolean isReservedSupported(String volumeUuid, String callingPackage);

    long getTotalBytes(String volumeUuid, String callingPackage);

    long getFreeBytes(String volumeUuid, String callingPackage);

    long getCacheBytes(String volumeUuid, String callingPackage);

    long getCacheQuotaBytes(String volumeUuid, int uid, String callingPackage);

    StorageStats queryStatsForPackage(String volumeUuid, String packageName, int userId, String callingPackage);

    StorageStats queryStatsForUid(String volumeUuid, int uid, String callingPackage);

    StorageStats queryStatsForUser(String volumeUuid, int userId, String callingPackage);

    ExternalStorageStats queryExternalStatsForUser(String volumeUuid, int userId, String callingPackage);

    ParceledListSlice queryCratesForPackage(String volumeUuid, String packageName,
                                            int userId, String callingPackage);

    ParceledListSlice queryCratesForUid(String volumeUuid, int uid,
                                        String callingPackage);

    ParceledListSlice queryCratesForUser(String volumeUuid, int userId,
                                         String callingPackage);


    abstract class Stub extends Binder implements IStorageStatsManager {

        public static IStorageStatsManager asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}
