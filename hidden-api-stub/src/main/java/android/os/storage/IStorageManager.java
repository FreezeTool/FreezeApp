package android.os.storage;
import android.content.res.ObbInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import com.android.internal.os.AppFuseMount;

import android.app.PendingIntent;


/**
 * WARNING! Update IMountService.h and IMountService.cpp if you change this
 * file. particular, the transaction ids below must match the
 * _TRANSACTION enum IMountService.cpp
 *
 * @hide - Applications should use android.os.storage.StorageManager to access
 * storage functions.
 */
interface IStorageManager {
    /**
     * Registers an IStorageEventListener for receiving async notifications.
     */
    void registerListener(IStorageEventListener listener);

    /**
     * Unregisters an IStorageEventListener
     */
    void unregisterListener(IStorageEventListener listener);

    /**
     * Shuts down the StorageManagerService and gracefully unmounts all external media.
     * Invokes call back once the shutdown is complete.
     */
    void shutdown(IStorageShutdownObserver observer);

    /**
     * Mounts an Opaque Binary Blob (OBB). Only allows the calling process's UID
     * access to the contents. StorageManagerService will call back to the
     * supplied IObbActionListener to inform it of the terminal state of the
     * call.
     */
    void mountObb(String rawPath, String canonicalPath, IObbActionListener token,
                  int nonce, ObbInfo obbInfo);

    /**
     * Unmounts an Opaque Binary Blob (OBB). When the force flag is specified,
     * any program using it will be forcibly killed to unmount the image.
     * StorageManagerService will call back to the supplied IObbActionListener to inform
     * it of the terminal state of the call.
     */
    void unmountObb(String rawPath, boolean force, IObbActionListener token, int nonce);

    /**
     * Checks whether the specified Opaque Binary Blob (OBB) is mounted
     * somewhere.
     */
    boolean isObbMounted(String rawPath);

    /**
     * Gets the path to the mounted Opaque Binary Blob (OBB).
     */
    String getMountedObbPath(String rawPath);

    /**
     * Returns list of all mountable volumes for the specified userId
     */
    StorageVolume[] getVolumeList(int userId, String callingPackage, int flags);

    /**
     * Ensure that all directories along given path exist, creating parent
     * directories as needed. Validates that given path is absolute and that it
     * contains no relative "." or ".." paths or symlinks. Also ensures that
     * path belongs to a volume managed by vold, and that path is either
     * external storage data or OBB directory belonging to calling app.
     */
    void mkdirs(String callingPkg, String path);

    long lastMaintenance();


    void runMaintenance();

    DiskInfo[] getDisks();

    VolumeInfo[] getVolumes(int flags);

    VolumeRecord[] getVolumeRecords(int flags);

    void mount(String volId);

    void unmount(String volId);

    void format(String volId);

    void partitionPublic(String diskId);

    void partitionPrivate(String diskId);

    void partitionMixed(String diskId, int ratio);

    void setVolumeNickname(String fsUuid, String nickname);

    void setVolumeUserFlags(String fsUuid, int flags, int mask);

    void forgetVolume(String fsUuid);

    void forgetAllVolumes();

    String getPrimaryStorageUuid();

    void setPrimaryStorageUuid(String volumeUuid, IPackageMoveObserver callback);

//    void benchmark(String volId, IVoldTaskListener listener);

    void setDebugFlags(int flags, int mask);

    void createUserKey(int userId, int serialNumber, boolean ephemeral);

    void destroyUserKey(int userId);

    void unlockUserKey(int userId, int serialNumber, byte[]secret);

    void lockUserKey(int userId);

    boolean isUserKeyUnlocked(int userId);

    void prepareUserStorage(String volumeUuid, int userId, int serialNumber, int flags);

    void destroyUserStorage(String volumeUuid, int userId, int flags);

    void addUserKeyAuth(int userId, int serialNumber, byte[]secret);

    void fixateNewestUserKeyAuth(int userId);

//    void fstrim(int flags, IVoldTaskListener listener);

    AppFuseMount mountProxyFileDescriptorBridge();

    ParcelFileDescriptor openProxyFileDescriptor(int mountPointId, int fileId, int mode);

    long getCacheQuotaBytes(String volumeUuid, int uid);

    long getCacheSizeBytes(String volumeUuid, int uid);

    long getAllocatableBytes(String volumeUuid, int flags, String callingPackage);

    void allocateBytes(String volumeUuid, long bytes, int flags, String callingPackage);

    void runIdleMaintenance();

    void abortIdleMaintenance();

    void commitChanges();

    boolean supportsCheckpoint();

    void startCheckpoint(int numTries);

    boolean needsCheckpoint();

    void abortChanges(String message, boolean retry);

    void clearUserKeyAuth(int userId, int serialNumber, byte[]secret);

    void fixupAppDir(String path);

    void disableAppDataIsolation(String pkgName, int pid, int userId);

    PendingIntent getManageSpaceActivityIntent(String packageName, int requestCode);

    void notifyAppIoBlocked(String volumeUuid, int uid, int tid, int reason);

    void notifyAppIoResumed(String volumeUuid, int uid, int tid, int reason);

    int getExternalStorageMountMode(int uid, String packageName);

    boolean isAppIoBlocked(String volumeUuid, int uid, int tid, int reason);

    void setCloudMediaProvider(String authority);

    String getCloudMediaProvider();

    abstract class Stub extends Binder implements IStorageManager {

        public static IStorageManager asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}