package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.dex.IArtManager;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.PersistableBundle;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Map;

public interface IPackageManager extends IInterface {

    IPackageInstaller getPackageInstaller()
            throws RemoteException;

    boolean isPackageAvailable(String packageName, int userId)
            throws RemoteException;

    ApplicationInfo getApplicationInfo(String packageName, int flags, int userId)
            throws RemoteException;

    @RequiresApi(33)
    ApplicationInfo getApplicationInfo(String packageName, long flags, int userId)
            throws RemoteException;

    PackageInfo getPackageInfo(String packageName, int flags, int userId)
            throws RemoteException;

    @RequiresApi(33)
    PackageInfo getPackageInfo(String packageName, long flags, int userId)
            throws RemoteException;

    int getPackageUid(String packageName, int userId)
            throws RemoteException;

    @RequiresApi(24)
    int getPackageUid(String packageName, int flags, int userId)
            throws RemoteException;

    @RequiresApi(33)
    int getPackageUid(String packageName, long flags, int userId)
            throws RemoteException;

    String[] getPackagesForUid(int uid)
            throws RemoteException;

    ParceledListSlice<PackageInfo> getInstalledPackages(int flags, int userId)
            throws RemoteException;

    @RequiresApi(33)
    ParceledListSlice<PackageInfo> getInstalledPackages(long flags, int userId)
            throws RemoteException;

    ParceledListSlice<ApplicationInfo> getInstalledApplications(int flags, int userId)
            throws RemoteException;

    @RequiresApi(33)
    ParceledListSlice<ApplicationInfo> getInstalledApplications(long flags, int userId)
            throws RemoteException;

    int getUidForSharedUser(String sharedUserName)
            throws RemoteException;

    void grantRuntimePermission(String packageName, String permissionName, int userId)
            throws RemoteException;

    void revokeRuntimePermission(String packageName, String permissionName, int userId)
            throws RemoteException;

    int getPermissionFlags(String permissionName, String packageName, int userId)
            throws RemoteException;

    void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, int userId)
            throws RemoteException;

    @RequiresApi(29)
    void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, int userId)
            throws RemoteException;

    int checkPermission(String permName, String pkgName, int userId)
            throws RemoteException;

    int checkUidPermission(String permName, int uid)
            throws RemoteException;

    boolean getApplicationHiddenSettingAsUser(String packageName, int userId)
            throws RemoteException;

    ProviderInfo resolveContentProvider(String name, int flags, int userId)
            throws RemoteException;

    @RequiresApi(33)
    ProviderInfo resolveContentProvider(String name, long flags, int userId)
            throws RemoteException;

    int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason)
            throws RemoteException;

    @RequiresApi(29)
    int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason, List<String> whiteListedPermissions)
            throws RemoteException;

    ParceledListSlice<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId)
            throws RemoteException;

    @RequiresApi(33)
    ParceledListSlice<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, long flags, int userId)
            throws RemoteException;

    boolean performDexOptMode(String packageName, boolean checkProfiles, String targetCompilerFilter, boolean force, boolean bootComplete, String splitName)
            throws RemoteException;

    int checkSignatures(String pkg1, String pkg2)
            throws RemoteException;

    int checkUidSignatures(int uid1, int uid2)
            throws RemoteException;

    PermissionGroupInfo getPermissionGroupInfo(String groupName, int flags)
            throws RemoteException;

    PermissionInfo getPermissionInfo(String permissionName, int flags)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.O)
    PermissionInfo getPermissionInfo(String permissionName, String packageName, int flags)
            throws RemoteException;

    abstract class Stub extends Binder implements IPackageManager {

        public static IPackageManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }


    void checkPackageStartable(String packageName, int userId);

    PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage,
                                        long flags, int userId);


    int[] getPackageGids(String packageName, long flags, int userId);


    String[] currentToCanonicalPackageNames(String[] names);

    String[] canonicalToCurrentPackageNames(String[] names);

    /**
     * @return the target SDK for the given package name, or -1 if it cannot be retrieved
     */
    int getTargetSdkVersion(String packageName);

    ActivityInfo getActivityInfo(ComponentName className, long flags, int userId);

    boolean activitySupportsIntent(ComponentName className, Intent intent,
                                   String resolvedType);

    ActivityInfo getReceiverInfo(ComponentName className, long flags, int userId);

    ServiceInfo getServiceInfo(ComponentName className, long flags, int userId);

    ProviderInfo getProviderInfo(ComponentName className, long flags, int userId);

    boolean isProtectedBroadcast(String actionName);

    List<String> getAllPackages();

    String getNameForUid(int uid);

    String[] getNamesForUids(int[] uids);

    int getFlagsForUid(int uid);

    int getPrivateFlagsForUid(int uid);


    boolean isUidPrivileged(int uid);

    ResolveInfo resolveIntent(Intent intent, String resolvedType, long flags, int userId);

    ResolveInfo findPersistentPreferredActivity(Intent intent, int userId);

    boolean canForwardTo(Intent intent, String resolvedType, int sourceUserId, int targetUserId);

    ParceledListSlice queryIntentActivityOptions(
            ComponentName caller, Intent[] specifics,
            String[] specificTypes, Intent intent,
            String resolvedType, long flags, int userId);

    ParceledListSlice queryIntentReceivers(Intent intent,
                                           String resolvedType, long flags, int userId);

    ResolveInfo resolveService(Intent intent,
                               String resolvedType, long flags, int userId);

    ParceledListSlice queryIntentServices(Intent intent,
                                          String resolvedType, long flags, int userId);

    ParceledListSlice queryIntentContentProviders(Intent intent,
                                                  String resolvedType, long flags, int userId);

    /**
     * This implements getPackagesHoldingPermissions via a "last returned row"
     * mechanism that is not exposed the API. This is to get around the IPC
     * limit that kicks when flags are included that bloat up the data
     * returned.
     */
    ParceledListSlice getPackagesHoldingPermissions(String[] permissions,
                                                    long flags, int userId);

    /**
     * This implements getInstalledApplications via a "last returned row"
     * mechanism that is not exposed the API. This is to get around the IPC
     * limit that kicks when flags are included that bloat up the data
     * returned.
     */

    /**
     * Retrieve all applications that are marked as persistent.
     *
     * @return A List<ApplicationInfo> containing one entry for each persistent
     * application.
     */
    ParceledListSlice getPersistentApplications(int flags);


    void querySyncProviders(List<String> outNames,
                            List<ProviderInfo> outInfo);

    ParceledListSlice queryContentProviders(
            String processName, int uid, long flags, String metaDataKey);


    InstrumentationInfo getInstrumentationInfo(
            ComponentName className, int flags);

    ParceledListSlice queryInstrumentation(
            String targetPackage, int flags);

    void finishPackageInstall(int token, boolean didLaunch);


    void setInstallerPackageName(String targetPackage, String installerPackageName);

    void setApplicationCategoryHint(String packageName, int categoryHint, String callerPackageName);

    void deletePackageAsUser(String packageName, int versionCode,
                             IPackageDeleteObserver observer, int userId, int flags);

    void deletePackageVersioned(VersionedPackage versionedPackage,
                                IPackageDeleteObserver2 observer, int userId, int flags);

    void deleteExistingPackageAsUser(VersionedPackage versionedPackage,
                                     IPackageDeleteObserver2 observer, int userId);


    String getInstallerPackageName(String packageName);

    InstallSourceInfo getInstallSourceInfo(String packageName);

    void resetApplicationPreferences(int userId);


    ResolveInfo getLastChosenActivity(Intent intent,
                                      String resolvedType, int flags);


    void setLastChosenActivity(Intent intent, String resolvedType, int flags,
                               IntentFilter filter, int match, ComponentName activity);

    void addPreferredActivity(IntentFilter filter, int match,
                              ComponentName[] set, ComponentName activity, int userId, boolean removeExisting);


    void replacePreferredActivity(IntentFilter filter, int match,
                                  ComponentName[] set, ComponentName activity, int userId);


    void clearPackagePreferredActivities(String packageName);


    int getPreferredActivities(List<IntentFilter> outFilters,
                               List<ComponentName> outActivities, String packageName);

    void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity, int userId);

    void clearPackagePersistentPreferredActivities(String packageName, int userId);

    void addCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage,
                                     int sourceUserId, int targetUserId, int flags);

    void clearCrossProfileIntentFilters(int sourceUserId, String ownerPackage);

    String[] setDistractingPackageRestrictionsAsUser(String[] packageNames, int restrictionFlags,
                                                     int userId);

    String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended,
                                        PersistableBundle appExtras, PersistableBundle launcherExtras,
                                        SuspendDialogInfo dialogInfo, String callingPackage, int userId);

    String[] getUnsuspendablePackagesForUser(String[] packageNames, int userId);

    boolean isPackageSuspendedForUser(String packageName, int userId);

    Bundle getSuspendedPackageAppExtras(String packageName, int userId);

    /**
     * Backup/restore support - only the system uid may use these.
     */
    byte[] getPreferredActivityBackup(int userId);

    void restorePreferredActivities(byte[] backup, int userId);

    byte[] getDefaultAppsBackup(int userId);

    void restoreDefaultApps(byte[] backup, int userId);

    byte[] getDomainVerificationBackup(int userId);

    void restoreDomainVerification(byte[] backup, int userId);

    /**
     * Report the set of 'Home' activity candidates, plus (if any) which of them
     * is the current "always use this one" setting.
     */

    ComponentName getHomeActivities(List<ResolveInfo> outHomeCandidates);

    void setHomeActivity(ComponentName className, int userId);

    /**
     * Overrides the label and icon of the component specified by the component name. The component
     * must belong to the calling app.
     * <p>
     * These changes will be reset on the next boot and whenever the package is updated.
     * <p>
     * Only the app defined as com.android.internal.R.config_overrideComponentUiPackage is allowed
     * to call this.
     *
     * @param componentName     The component name to override the label/icon of.
     * @param nonLocalizedLabel The label to be displayed.
     * @param icon              The icon to be displayed.
     * @param userId            The user id.
     */
    void overrideLabelAndIcon(ComponentName componentName, String nonLocalizedLabel,
                              int icon, int userId);

    /**
     * Restores the label and icon of the activity specified by the component name if either has
     * been overridden. The component must belong to the calling app.
     * <p>
     * Only the app defined as com.android.internal.R.config_overrideComponentUiPackage is allowed
     * to call this.
     *
     * @param componentName The component name.
     * @param userId        The user id.
     */
    void restoreLabelAndIcon(ComponentName componentName, int userId);

    /**
     * As per {@link android.content.pm.PackageManager#setComponentEnabledSetting}.
     */

    void setComponentEnabledSetting(ComponentName componentName,
                                    int newState, int flags, int userId);

    /**
     * As per {@link android.content.pm.PackageManager#setComponentEnabledSettings}.
     */
    void setComponentEnabledSettings(List<PackageManager.ComponentEnabledSetting> settings, int userId);

    /**
     * As per {@link android.content.pm.PackageManager#getComponentEnabledSetting}.
     */

    int getComponentEnabledSetting(ComponentName componentName, int userId);

    /**
     * As per {@link android.content.pm.PackageManager#setApplicationEnabledSetting}.
     */

    void setApplicationEnabledSetting(String packageName, int newState, int flags,
                                      int userId, String callingPackage) throws RemoteException;

    /**
     * As per {@link android.content.pm.PackageManager#getApplicationEnabledSetting}.
     */

    int getApplicationEnabledSetting(String packageName, int userId);

    /**
     * Logs process start information (including APK hash) to the security log.
     */
    void logAppProcessStartIfNeeded(String packageName, String processName, int uid, String seinfo, String apkFile, int pid);

    /**
     *
     */
    void flushPackageRestrictionsAsUser(int userId);

    /**
     * Set whether the given package should be considered stopped, making
     * it not visible to implicit intents that filter out stopped packages.
     */

    void setPackageStoppedState(String packageName, boolean stopped, int userId);


    void freeStorageAndNotify(String volumeUuid, long freeStorageSize,
                              int storageFlags, IPackageDataObserver observer);


    void freeStorage(String volumeUuid, long freeStorageSize,
                     int storageFlags, IntentSender pi);

    /**
     * Delete all the cache files an applications cache directory
     *
     * @param packageName The package name of the application whose cache
     *                    files need to be deleted
     * @param observer    a callback used to notify when the deletion is finished.
     */

    void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer);

    /**
     * Delete all the cache files an applications cache directory
     *
     * @param packageName The package name of the application whose cache
     *                    files need to be deleted
     * @param userId      the user to delete application cache for
     * @param observer    a callback used to notify when the deletion is finished.
     */
    void deleteApplicationCacheFilesAsUser(String packageName, int userId, IPackageDataObserver observer);

    /**
     * Clear the user data directory of an application.
     *
     * @param packageName The package name of the application whose cache
     *                    files need to be deleted
     * @param observer    a callback used to notify when the operation is completed.
     */
    void clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId);

    /**
     * Clear the profile data of an application.
     *
     * @param packageName The package name of the application whose profile data
     *                    need to be deleted
     */
    void clearApplicationProfileData(String packageName);


    void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer);

    /**
     * Get a list of shared libraries that are available on the
     * system.
     */

    String[] getSystemSharedLibraryNames();

    /**
     * Get a list of features that are available on the
     * system.
     */
    ParceledListSlice getSystemAvailableFeatures();

    boolean hasSystemFeature(String name, int version);

    void enterSafeMode();

    boolean isSafeMode();

    boolean hasSystemUidErrors();

    void notifyPackageUse(String packageName, int reason);


    void notifyDexLoad(String loadingPackageName,
                       Map<String, String> classLoaderContextMap, String loaderIsa);

    void registerDexModule(String packageName, String dexModulePath,
                           boolean isSharedModule, IDexModuleRegisterCallback callback);


    /**
     * Ask the package manager to perform a dex-opt with the given compiler filter on the
     * secondary dex files belonging to the given package.
     * <p>
     * Note: exposed only for the shell command to allow moving packages explicitly to a
     * definite state.
     */
    boolean performDexOptSecondary(String packageName,
                                   String targetCompilerFilter, boolean force);

    /**
     * Ask the package manager to dump profiles associated with a package.
     *
     * @param packageName           The name of the package to dump.
     * @param dumpClassesAndMethods If false, pass {@code --dump-only} to profman to dump the
     *                              profile a human readable form intended for debugging. If true, pass
     *                              {@code --dump-classes-and-methods} to profman to dump a sorted list of classes and methods
     *                              a human readable form that is valid input for {@code profman --create-profile-from}.
     */
    void dumpProfiles(String packageName, boolean dumpClassesAndMethods);

    void forceDexOpt(String packageName);

    /**
     * Reconcile the information we have about the secondary dex files belonging to
     * {@code packagName} and the actual dex files. For all dex files that were
     * deleted, update the internal records and delete the generated oat files.
     */
    void reconcileSecondaryDexFiles(String packageName);

    int getMoveStatus(int moveId);

    void registerMoveCallback(IPackageMoveObserver callback);

    void unregisterMoveCallback(IPackageMoveObserver callback);

    int movePackage(String packageName, String volumeUuid);

    int movePrimaryStorage(String volumeUuid);

    boolean setInstallLocation(int loc);

    int getInstallLocation();


    void verifyPendingInstall(int id, int verificationCode);

    void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay);

    /**
     * @deprecated
     */
    void verifyIntentFilter(int id, int verificationCode, List<String> failedDomains);

    /**
     * @deprecated
     */
    int getIntentVerificationStatus(String packageName, int userId);

    /**
     * @deprecated
     */
    boolean updateIntentVerificationStatus(String packageName, int status, int userId);

    /**
     * @deprecated
     */
    ParceledListSlice getIntentFilterVerifications(String packageName);

    ParceledListSlice getAllIntentFilters(String packageName);

    VerifierDeviceIdentity getVerifierDeviceIdentity();

    boolean isFirstBoot();

    boolean isOnlyCoreApps();

    boolean isDeviceUpgrading();

    /**
     * Reflects current DeviceStorageMonitorService state
     */

    boolean isStorageLow();


    boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId);

    void setSystemAppHiddenUntilInstalled(String packageName, boolean hidden);

    boolean setSystemAppInstallState(String packageName, boolean installed, int userId);


    boolean setBlockUninstallForUser(String packageName, boolean blockUninstall, int userId);

    boolean getBlockUninstallForUser(String packageName, int userId);

    KeySet getKeySetByAlias(String packageName, String alias);

    KeySet getSigningKeySet(String packageName);

    boolean isPackageSignedByKeySet(String packageName, KeySet ks);

    boolean isPackageSignedByKeySetExactly(String packageName, KeySet ks);

    String getPermissionControllerPackageName();

    String getSdkSandboxPackageName();

    ParceledListSlice getInstantApps(int userId);

    byte[] getInstantAppCookie(String packageName, int userId);

    boolean setInstantAppCookie(String packageName, byte[] cookie, int userId);

    Bitmap getInstantAppIcon(String packageName, int userId);

    boolean isInstantApp(String packageName, int userId);

    boolean setRequiredForSystemUser(String packageName, boolean systemUserApp);

    /**
     * Sets whether or not an update is available. Ostensibly for instant apps
     * to force exteranl resolution.
     */
    void setUpdateAvailable(String packageName, boolean updateAvaialble);

    String getServicesSystemSharedLibraryPackageName();

    String getSharedSystemSharedLibraryPackageName();

    ChangedPackages getChangedPackages(int sequenceNumber, int userId);

    boolean isPackageDeviceAdminOnAnyUser(String packageName);

    int getInstallReason(String packageName, int userId);

    ParceledListSlice getSharedLibraries(String packageName, long flags, int userId);

    ParceledListSlice getDeclaredSharedLibraries(String packageName, long flags, int userId);

    boolean canRequestPackageInstalls(String packageName, int userId);

    void deletePreloadsFileCache();

    ComponentName getInstantAppResolverComponent();

    ComponentName getInstantAppResolverSettingsComponent();

    ComponentName getInstantAppInstallerComponent();

    String getInstantAppAndroidId(String packageName, int userId);

    IArtManager getArtManager();

    void setHarmfulAppWarning(String packageName, CharSequence warning, int userId);

    CharSequence getHarmfulAppWarning(String packageName, int userId);

    boolean hasSigningCertificate(String packageName, byte[] signingCertificate, int flags);

    boolean hasUidSigningCertificate(int uid, byte[] signingCertificate, int flags);

    String getDefaultTextClassifierPackageName();

    String getSystemTextClassifierPackageName();

    String getAttentionServicePackageName();

    String getRotationResolverPackageName();

    String getWellbeingPackageName();

    String getAppPredictionServicePackageName();

    String getSystemCaptionsServicePackageName();

    String getSetupWizardPackageName();

    String getIncidentReportApproverPackageName();

    String getContentCaptureServicePackageName();

    boolean isPackageStateProtected(String packageName, int userId);

    void sendDeviceCustomizationReadyBroadcast();

    List<ModuleInfo> getInstalledModules(int flags);

    ModuleInfo getModuleInfo(String packageName, int flags);

    int getRuntimePermissionsVersion(int userId);

    void setRuntimePermissionsVersion(int version, int userId);

    void notifyPackagesReplacedReceived(String[] packages);

    void requestPackageChecksums(String packageName, boolean includeSplits, int optional, int required, List trustedInstallers, IOnChecksumsReadyListener onChecksumsReadyListener, int userId);

    IntentSender getLaunchIntentSenderForPackage(String packageName, String callingPackage,
                                                 String featureId, int userId);


    String[] getAppOpPermissionPackages(String permissionName);


    boolean addPermission(PermissionInfo info);


    boolean addPermissionAsync(PermissionInfo info);

    void removePermission(String name);

    void setMimeGroup(String packageName, String group, List<String> mimeTypes);

    String getSplashScreenTheme(String packageName, int userId);

    void setSplashScreenTheme(String packageName, String themeName, int userId);

    List<String> getMimeGroup(String packageName, String group);

    boolean isAutoRevokeWhitelisted(String packageName);

    void makeProviderVisible(int recipientAppId, String visibleAuthority);

    void makeUidVisible(int recipientAppId, int visibleUid);

    IBinder getHoldLockToken();

    void holdLock(IBinder token, int durationMs);

    PackageManager.Property getProperty(String propertyName, String packageName, String className);

    ParceledListSlice queryProperty(String propertyName, int componentType);

    void setKeepUninstalledPackages(List<String> packageList);

    boolean canPackageQuery(String sourcePackageName, String targetPackageName, int userId);
}
