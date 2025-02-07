package android.os;

import android.app.usage.IStorageStatsManager;

public interface IInstalld {
    void createUserData(String uuid, int userId, int userSerial, int flags);

    void destroyUserData(String uuid, int userId, int flags);

    void setFirstBoot();

    android.os.CreateAppDataResult createAppData(android.os.CreateAppDataArgs args);

    android.os.CreateAppDataResult[] createAppDataBatched(android.os.CreateAppDataArgs[] args);

    void reconcileSdkData(android.os.ReconcileSdkDataArgs args);

    void restoreconAppData(String uuid, String packageName,
                           int userId, int flags, int appId, String seInfo);

    void migrateAppData(String uuid, String packageName,
                        int userId, int flags);

    void clearAppData(String uuid, String packageName,
                      int userId, int flags, long ceDataInode);

    void destroyAppData(String uuid, String packageName,
                        int userId, int flags, long ceDataInode);

    void fixupAppData(String uuid, int flags);

    long[] getAppSize(String uuid, String[] packageNames,
                      int userId, int flags, int appId, long[] ceDataInodes,
                      String[] codePaths);

    long[] getUserSize(String uuid, int userId, int flags, int[] appIds);

    long[] getExternalSize(String uuid, int userId, int flags, int[] appIds);


    android.os.storage.CrateMetadata[] getAppCrates(
            String uuid, String[] packageNames,
            int userId);

    android.os.storage.CrateMetadata[] getUserCrates(
            String uuid, int userId);

    void setAppQuota(String uuid, int userId, int appId, long cacheQuota);

    void moveCompleteApp(String fromUuid, String toUuid,
                         String packageName, int appId,
                         String seInfo, int targetSdkVersion, String fromCodePath);

    boolean dexopt(String apkPath, int uid, String packageName,
                   String instructionSet, int dexoptNeeded,
                   String outputPath, int dexFlags,
                   String compilerFilter, String uuid,
                   String sharedLibraries,
                   String seInfo, boolean downgrade, int targetSdkVersion,
                   String profileName,
                   String dexMetadataPath,
                   String compilationReason);

    void controlDexOptBlocking(boolean block);

    boolean compileLayouts(String apkPath, String packageName,
                           String outDexFile, int uid);

    void rmdex(String codePath, String instructionSet);

    int mergeProfiles(int uid, String packageName, String profileName);

    boolean dumpProfiles(int uid, String packageName, String profileName,
                         String codePath, boolean dumpClassesAndMethods);

    boolean copySystemProfile(String systemProfile, int uid,
                              String packageName, String profileName);

    void clearAppProfiles(String packageName, String profileName);

    void destroyAppProfiles(String packageName);

    void deleteReferenceProfile(String packageName, String profileName);

    boolean createProfileSnapshot(int appId, String packageName,
                                  String profileName, String classpath);

    void destroyProfileSnapshot(String packageName, String profileName);

    void rmPackageDir(String packageName, String packageDir);

    void freeCache(String uuid, long targetFreeBytes, int flags);

    void linkNativeLibraryDirectory(String uuid,
                                    String packageName, String nativeLibPath32, int userId);

    void createOatDir(String packageName, String oatDir,
                      String instructionSet);

    void linkFile(String packageName, String relativePath,
                  String fromBase, String toBase);

    void moveAb(String packageName, String apkPath,
                String instructionSet, String outputPath);

    long deleteOdex(String packageName, String apkPath,
                    String instructionSet, String outputPath);

    boolean reconcileSecondaryDexFile(String dexPath, String pkgName,
                                      int uid, String[] isas, String volume_uuid,
                                      int storage_flag);

    byte[] hashSecondaryDexFile(String dexPath, String pkgName,
                                int uid, String volumeUuid, int storageFlag);

    void invalidateMounts();

    boolean isQuotaSupported(String uuid);

    boolean prepareAppProfile(String packageName,
                              int userId, int appId, String profileName, String codePath,
                              String dexMetadata);

    long snapshotAppData(String uuid, String packageName,
                         int userId, int snapshotId, int storageFlags);

    void restoreAppDataSnapshot(String uuid, String packageName,
                                int appId, String seInfo, int user, int snapshotId, int storageflags);

    void destroyAppDataSnapshot(String uuid, String packageName,
                                int userId, long ceSnapshotInode, int snapshotId, int storageFlags);

    void destroyCeSnapshotsNotSpecified(String uuid, int userId,
                                        int[] retainSnapshotIds);

    void tryMountDataMirror(String volumeUuid);

    void onPrivateVolumeRemoved(String volumeUuid);

    void migrateLegacyObbData();

    void cleanupInvalidPackageDirs(String uuid, int userId, int flags);

    int getOdexVisibility(String packageName, String apkPath,
                          String instructionSet, String outputPath);

    int FLAG_STORAGE_DE = 0x1;
    int FLAG_STORAGE_CE = 0x2;
    int FLAG_STORAGE_EXTERNAL = 0x4;
    int FLAG_STORAGE_SDK = 0x8;

    int FLAG_CLEAR_CACHE_ONLY = 0x10;
    int FLAG_CLEAR_CODE_CACHE_ONLY = 0x20;

    int FLAG_FREE_CACHE_V2 = 0x100;
    int FLAG_FREE_CACHE_V2_DEFY_QUOTA = 0x200;
    int FLAG_FREE_CACHE_NOOP = 0x400;

    int FLAG_FREE_CACHE_DEFY_TARGET_FREE_BYTES = 0x800;

    int FLAG_USE_QUOTA = 0x1000;
    int FLAG_FORCE = 0x2000;

    int FLAG_CLEAR_APP_DATA_KEEP_ART_PROFILES = 0x20000;


    abstract class Stub extends Binder implements IInstalld {

        public static IInstalld asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}