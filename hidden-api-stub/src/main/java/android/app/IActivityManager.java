package android.app;

import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.LocusId;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.WorkSource;

import androidx.annotation.RequiresApi;

import java.util.List;

public interface IActivityManager extends IInterface {

    @RequiresApi(29)
    ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag)
            throws RemoteException;

    @RequiresApi(26)
    ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token)
            throws RemoteException;

    void removeContentProviderExternal(String name, IBinder token)
            throws RemoteException;

    int checkPermission(String permission, int pid, int uid)
            throws RemoteException;

    void registerProcessObserver(IProcessObserver observer)
            throws RemoteException;

    void unregisterProcessObserver(IProcessObserver observer)
            throws RemoteException;

    void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage)
            throws RemoteException;

    void unregisterUidObserver(IUidObserver observer)
            throws RemoteException;

    void forceStopPackage(String packageName, int userId)
            throws RemoteException;

    int startActivityAsUser(IApplicationThread caller, String callingPackage,
                            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
                            int requestCode, int flags, ProfilerInfo profilerInfo,
                            Bundle options, int userId)
            throws RemoteException;

    Intent registerReceiver(IApplicationThread caller, String callerPackage,
                            IIntentReceiver receiver, IntentFilter filter,
                            String requiredPermission, int userId)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.O)
    Intent registerReceiver(IApplicationThread caller, String callerPackage,
                            IIntentReceiver receiver, IntentFilter filter,
                            String requiredPermission, int userId, int flags)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.R)
    Intent registerReceiverWithFeature(
            IApplicationThread caller, String callerPackage,
            String callingFeatureId, IIntentReceiver receiver, IntentFilter filter,
            String requiredPermission, int userId, int flags)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.S)
    Intent registerReceiverWithFeature(
            IApplicationThread caller, String callerPackage,
            String callingFeatureId, String receiverId, IIntentReceiver receiver,
            IntentFilter filter, String requiredPermission, int userId, int flags)
            throws RemoteException;

    void unregisterReceiver(IIntentReceiver receiver)
            throws RemoteException;

    boolean isUserRunning(int userId, int flags)
            throws RemoteException;

    int broadcastIntent(IApplicationThread caller, Intent intent,
                        String resolvedType, IIntentReceiver resultTo, int resultCode,
                        String resultData, Bundle map, String[] requiredPermissions,
                        int appOp, Bundle options, boolean serialized, boolean sticky, int userId)
            throws RemoteException;

    @RequiresApi(26)
    int getUidProcessState(int uid, String callingPackage)
            throws RemoteException;

    int getPackageProcessState(String packageName, String callingPackage)
            throws RemoteException;

    /**
     * Method for the shell UID to start deletating its permission identity to an
     * active instrumenation. The shell can delegate permissions only to one active
     * instrumentation at a time. An active instrumentation is one running and
     * started from the shell.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    void startDelegateShellPermissionIdentity(int uid, String[] permissions)
            throws RemoteException;

    /**
     * Method for the shell UID to stop deletating its permission identity to an
     * active instrumenation. An active instrumentation is one running and
     * started from the shell.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    void stopDelegateShellPermissionIdentity()
            throws RemoteException;

    /**
     * Method for the shell UID to get currently adopted permissions for an active instrumentation.
     * An active instrumentation is one running and started from the shell.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    List<String> getDelegatedShellPermissions()
            throws RemoteException;

    void registerTaskStackListener(ITaskStackListener listener)
            throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener listener)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.S)
    ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo()
            throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, int flags)
            throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.P)
    List<ActivityManager.RunningTaskInfo> getTasks(int maxNum)
            throws RemoteException;

    @RequiresApi(26)
    abstract class Stub extends Binder implements IActivityManager {

        public static IActivityManager asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }

    ParcelFileDescriptor openContentUri(String uriString);


    boolean isUidActive(int uid, String callingPackage);

    //    void handleApplicationCrash(IBinder app,
//                                ApplicationErrorReport.ParcelableCrashInfo crashInfo);
    int startActivity(IApplicationThread caller, String callingPackage, Intent intent,
                      String resolvedType, IBinder resultTo, String resultWho, int requestCode,
                      int flags, ProfilerInfo profilerInfo, Bundle options);

    int startActivityWithFeature(IApplicationThread caller, String callingPackage,
                                 String callingFeatureId, Intent intent, String resolvedType,
                                 IBinder resultTo, String resultWho, int requestCode, int flags,
                                 ProfilerInfo profilerInfo, Bundle options);

    void unhandledBack();

    boolean finishActivity(IBinder token, int code, Intent data, int finishTask);

    int broadcastIntentWithFeature(IApplicationThread caller, String callingFeatureId,
                                   Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode,
                                   String resultData, Bundle map, String[] requiredPermissions, String[] excludePermissions,
                                   String[] excludePackages, int appOp, Bundle options, boolean serialized, boolean sticky, int userId);

    void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId);

    void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map,
                        boolean abortBroadcast, int flags);

    void attachApplication(IApplicationThread app, long startSeq);


    void moveTaskToFront(IApplicationThread caller, String callingPackage, int task,
                         int flags, Bundle options);


    int getTaskForActivity(IBinder token, boolean onlyRoot);

    ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage,
                                             String name, int userId, boolean stable);


    void publishContentProviders(IApplicationThread caller,
                                 List<ContentProviderHolder> providers);

    boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta);

    PendingIntent getRunningServiceControlPanel(ComponentName service);

    ComponentName startService(IApplicationThread caller, Intent service,
                               String resolvedType, boolean requireForeground, String callingPackage,
                               String callingFeatureId, int userId);


    int stopService(IApplicationThread caller, Intent service,
                    String resolvedType, int userId);

    // Currently keeping old bindService because it is on the greylist

//    int bindService(IApplicationThread caller, IBinder token, Intent service,
//                    String resolvedType, IServiceConnection connection, int flags,
//                    String callingPackage, int userId);
//
//    int bindServiceInstance(IApplicationThread caller, IBinder token, Intent service,
//                            String resolvedType, IServiceConnection connection, int flags,
//                            String instanceName, String callingPackage, int userId);
//
//    void updateServiceGroup(IServiceConnection connection, int group, int importance);
//
//    boolean unbindService(IServiceConnection connection);

    void publishService(IBinder token, Intent intent, IBinder service);

    void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent);

    void setAgentApp(String packageName, String agent);

    void setAlwaysFinish(boolean enabled);

//    boolean startInstrumentation(ComponentName className, String profileFile,
//                                 int flags, Bundle arguments, IInstrumentationWatcher watcher,
//                                 IUiAutomationConnection connection, int userId,
//                                 String abiOverride);

    void addInstrumentationResults(IApplicationThread target, Bundle results);

    void finishInstrumentation(IApplicationThread target, int resultCode,
                               Bundle results);

    Configuration getConfiguration();

    boolean updateConfiguration(Configuration values);

    boolean updateMccMncConfiguration(String mcc, String mnc);

    boolean stopServiceToken(ComponentName className, IBinder token, int startId);


    void setProcessLimit(int max);


    int getProcessLimit();

    int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId,
                           IBinder callerToken);

    int[] checkUriPermissions(List<Uri> uris, int pid, int uid, int mode, int userId,
                              IBinder callerToken);

    void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri,
                            int mode, int userId);

    void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri,
                             int mode, int userId);

//    void setActivityController(IActivityController watcher, boolean imAMonkey);

    void showWaitingForDebugger(IApplicationThread who, boolean waiting);

    /*
     * This will deliver the specified signal to all the persistent processes. Currently only
     * SIGUSR1 is delivered. All others are ignored.
     */
    void signalPersistentProcesses(int signal);


    ParceledListSlice getRecentTasks(int maxNum, int flags, int userId);


    void serviceDoneExecuting(IBinder token, int type, int startId, int res);


    IIntentSender getIntentSender(int type, String packageName, IBinder token,
                                  String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes,
                                  int flags, Bundle options, int userId);

    IIntentSender getIntentSenderWithFeature(int type, String packageName, String featureId,
                                             IBinder token, String resultWho, int requestCode, Intent[] intents,
                                             String[] resolvedTypes, int flags, Bundle options, int userId);

    void cancelIntentSender(IIntentSender sender);

//    ActivityManager.PendingIntentInfo getInfoForIntentSender(IIntentSender sender);

//    boolean registerIntentSenderCancelListenerEx(IIntentSender sender,
//                                                 IResultReceiver receiver);

//    void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver);

    void enterSafeMode();

    void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid,
                         String sourcePkg, String tag);


    void removeContentProvider(IBinder connection, boolean stable);


    void setRequestedOrientation(IBinder token, int requestedOrientation);

    void unbindFinished(IBinder token, Intent service, boolean doRebind);


    void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason);

    void setServiceForeground(ComponentName className, IBinder token,
                              int id, Notification notification, int flags, int foregroundServiceType);

    int getForegroundServiceType(ComponentName className, IBinder token);


    boolean moveActivityTaskToBack(IBinder token, boolean nonRoot);


    void getMemoryInfo(ActivityManager.MemoryInfo outInfo);

    List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState();

//    boolean clearApplicationUserData(String packageName, boolean keepState,
//                                     IPackageDataObserver observer, int userId);

    void stopAppForUser(String packageName, int userId);

    /**
     * Returns {@code false} if the callback could not be registered, {@true} otherwise.
     */
//    boolean registerForegroundServiceObserver(IForegroundServiceObserver callback);

    boolean killPids(int[] pids, String reason, boolean secure);


    List<ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags);

    // Retrieve running application processes the system

    List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

    IBinder peekService(Intent service, String resolvedType, String callingPackage);

    // Turn on/off profiling a particular process.
    boolean profileControl(String process, int userId, boolean start,
                           ProfilerInfo profilerInfo, int profileType);


    boolean shutdown(int timeout);


    void stopAppSwitches();

    void resumeAppSwitches();

    boolean bindBackupAgent(String packageName, int backupRestoreMode, int targetUserId,
                            int operationType);

    void backupAgentCreated(String packageName, IBinder agent, int userId);

    void unbindBackupAgent(ApplicationInfo appInfo);

    int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll,
                           boolean requireFull, String name, String callerPackage);

    void addPackageDependency(String packageName);

    void killApplication(String pkg, int appId, int userId, String reason);

    void closeSystemDialogs(String reason);

    Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids);

    void killApplicationProcess(String processName, int uid);

    // Special low-level communication with activity manager.
//    boolean handleApplicationWtf(IBinder app, String tag, boolean system,
//                                 ApplicationErrorReport.ParcelableCrashInfo crashInfo, int immediateCallerPid);
    void killBackgroundProcesses(String packageName, int userId);

    boolean isUserAMonkey();

    // Retrieve info of applications installed on external media that are currently
    // running.
    List<ApplicationInfo> getRunningExternalApplications();

    void finishHeavyWeightApp();

    // A StrictMode violation to be handled.
//    void handleApplicationStrictModeViolation(IBinder app, int penaltyMask,
//                                              StrictMode.ViolationInfo crashInfo);
    boolean isTopActivityImmersive();

    void crashApplicationWithType(int uid, int initialPid, String packageName, int userId,
                                  String message, boolean force, int exceptionTypeId);

    void crashApplicationWithTypeWithExtras(int uid, int initialPid, String packageName,
                                            int userId, String message, boolean force, int exceptionTypeId, Bundle extras);

    String getProviderMimeType(Uri uri, int userId);

    void getProviderMimeTypeAsync(Uri uri, int userId, RemoteCallback resultCallback);

    // Cause the specified process to dump the specified heap.
    boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo,
                     boolean runGc, String path, ParcelFileDescriptor fd,
                     RemoteCallback finishCallback);


    void setPackageScreenCompatMode(String packageName, int mode);

    boolean switchUser(int userid);

    String getSwitchingFromUserMessage();

    String getSwitchingToUserMessage();

    void setStopUserOnSwitch(int value);

    boolean removeTask(int taskId);

    boolean isIntentSenderTargetedToPackage(IIntentSender sender);

    void updatePersistentConfiguration(Configuration values);

    void updatePersistentConfigurationWithAttribution(Configuration values,
                                                      String callingPackageName, String callingAttributionTag);

    long[] getProcessPss(int[] pids);

    void showBootMessage(CharSequence msg, boolean always);

    void killAllBackgroundProcesses();

    void removeContentProviderExternalAsUser(String name, IBinder token, int userId);

    // Get memory information about the calling process.
    void getMyMemoryState(ActivityManager.RunningAppProcessInfo outInfo);

    boolean killProcessesBelowForeground(String reason);

    UserInfo getCurrentUser();

    int getCurrentUserId();

    // This is not public because you need to be very careful how you
    // manage your activity to make sure it is always the uid you expect.
    int getLaunchedFromUid(IBinder activityToken);

    void unstableProviderDied(IBinder connection);

    boolean isIntentSenderAnActivity(IIntentSender sender);

    int startActivityAsUserWithFeature(IApplicationThread caller, String callingPackage,
                                       String callingFeatureId, Intent intent, String resolvedType,
                                       IBinder resultTo, String resultWho, int requestCode, int flags,
                                       ProfilerInfo profilerInfo, Bundle options, int userId);

//    int stopUser(int userid, boolean force, IStopUserCallback callback);

//    int stopUserWithDelayedLocking(int userid, boolean force, IStopUserCallback callback);


    //    void registerUserSwitchObserver(IUserSwitchObserver observer, String name);
//    void unregisterUserSwitchObserver(IUserSwitchObserver observer);
    int[] getRunningUserIds();

    // Request a heap dump for the system server.
    void requestSystemServerHeapDump();

    void requestBugReport(int bugreportType);

    void requestBugReportWithDescription(String shareTitle,
                                         String shareDescription, int bugreportType);

    /**
     * Takes a telephony bug report and notifies the user with the title and description
     * that are passed to this API as parameters
     *
     * @param shareTitle       should be a valid legible string less than 50 chars long
     * @param shareDescription should be less than 150 chars long
     * @throws IllegalArgumentException if shareTitle or shareDescription is too big or if the
     *                                  paremeters cannot be encoding to an UTF-8 charset.
     */
    void requestTelephonyBugReport(String shareTitle, String shareDescription);

    /**
     * This method is only used by Wifi.
     * <p>
     * Takes a minimal bugreport of Wifi-related state.
     *
     * @param shareTitle       should be a valid legible string less than 50 chars long
     * @param shareDescription should be less than 150 chars long
     * @throws IllegalArgumentException if shareTitle or shareDescription is too big or if the
     *                                  parameters cannot be encoding to an UTF-8 charset.
     */
    void requestWifiBugReport(String shareTitle, String shareDescription);

    void requestInteractiveBugReportWithDescription(String shareTitle,
                                                    String shareDescription);

    void requestInteractiveBugReport();

    void requestFullBugReport();

    void requestRemoteBugReport(long nonce);

    boolean launchBugReportHandlerApp();

    List<String> getBugreportWhitelistedPackages();

    Intent getIntentForIntentSender(IIntentSender sender);

    // This is not public because you need to be very careful how you
    // manage your activity to make sure it is always the uid you expect.
    String getLaunchedFromPackage(IBinder activityToken);

    void killUid(int appId, int userId, String reason);

    void setUserIsMonkey(boolean monkey);

    void hang(IBinder who, boolean allowRestart);

    List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos();

    void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop);

    void setFocusedRootTask(int taskId);

    void restart();

    void performIdleMaintenance();

    void appNotRespondingViaProvider(IBinder connection);

    Rect getTaskBounds(int taskId);

    boolean setProcessMemoryTrimLevel(String process, int userId, int level);


    // Start of L transactions
    String getTagForIntentSender(IIntentSender sender, String prefix);

    boolean startUserInBackground(int userid);

    boolean isInLockTaskMode();

    int startActivityFromRecents(int taskId, Bundle options);

    void startSystemLockTaskMode(int taskId);

    boolean isTopOfTask(IBinder token);

    void bootAnimationComplete();

    void notifyCleartextNetwork(int uid, byte[] firstPacket);

    void setTaskResizeable(int taskId, int resizeableMode);

    void resizeTask(int taskId, Rect bounds, int resizeMode);

    int getLockTaskModeState();

    void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize,
                               String reportPackage);

    void dumpHeapFinished(String path);

    void updateLockTaskPackages(int userId, String[] packages);

    void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag);

    void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag);

    // Start of N transactions
    // Start Binder transaction tracking for all applications.
    boolean startBinderTracking();

    // Stop Binder transaction tracking for all applications and dump trace data to the given file
    // descriptor.
    boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd);

    void enableBinderTracing();

    void suppressResizeConfigChanges(boolean suppress);

    //    boolean unlockUser(int userid, byte[] token, byte[] secret,
//                       IProgressListener listener);
    void killPackageDependents(String packageName, int userId);

    void makePackageIdle(String packageName, int userId);

    int getMemoryTrimLevel();

    boolean isVrModePackageEnabled(ComponentName packageName);

    void notifyLockedProfile(int userId);

    void startConfirmDeviceCredentialIntent(Intent intent, Bundle options);

    void sendIdleJobTrigger();

    int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code,
                         Intent intent, String resolvedType, IIntentReceiver finishedReceiver,
                         String requiredPermission, Bundle options);

    boolean isBackgroundRestricted(String packageName);

    // Start of N MR1 transactions
    void setRenderThread(int tid);

    /**
     * Lets activity manager know whether the calling process is currently showing "top-level" UI
     * that is not an activity, i.e. windows on the screen the user is currently interacting with.
     *
     * <p>This flag can only be set for persistent processes.
     *
     * @param hasTopUi Whether the calling process has "top-level" UI.
     */
    void setHasTopUi(boolean hasTopUi);

    // Start of O transactions
    int restartUserInBackground(int userId);

    /**
     * Cancels the window transitions for the given task.
     */
    void cancelTaskWindowTransition(int taskId);

    void scheduleApplicationInfoChanged(List<String> packageNames, int userId);

    void setPersistentVrThread(int tid);

    void waitForNetworkStateUpdate(long procStateSeq);

    /**
     * Add a bare uid to the background restrictions whitelist.  Only the system uid may call this.
     */
    void backgroundAllowlistUid(int uid);

    // Start of P transactions

    /**
     * Similar to {@link #startUserInBackground(int userId), but with a listener to report
     * user unlock progress.
     */
//    boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener);


    ParcelFileDescriptor getLifeMonitor();

    /**
     * Start user, if it us not already running, and bring it to foreground.
     * unlockProgressListener can be null if monitoring progress is not necessary.
     */
//    boolean startUserInForegroundWithListener(int userid, IProgressListener unlockProgressListener);

    /**
     * Method for the app to tell system that it's wedged and would like to trigger an ANR.
     */
    void appNotResponding(String reason);

    /**
     * Return a list of {@link ApplicationExitInfo} records.
     *
     * <p class="note"> Note: System stores these historical information a ring buffer, older
     * records would be overwritten by newer records. </p>
     *
     * <p class="note"> Note: the case that this application bound to an external service with
     * flag {@link android.content.Context#BIND_EXTERNAL_SERVICE}, the process of that external
     * service will be included this package's exit info. </p>
     *
     * @param packageName Optional, an empty value means match all packages belonging to the
     *                    caller's UID. If this package belongs to another UID, you must hold
     *                    {@link android.Manifest.permission#DUMP} order to retrieve it.
     * @param pid         Optional, it could be a process ID that used to belong to this package but
     *                    died later; A value of 0 means to ignore this parameter and return all
     *                    matching records.
     * @param maxNum      Optional, the maximum number of results should be returned; A value of 0
     *                    means to ignore this parameter and return all matching records
     * @param userId      The userId the multi-user environment.
     * @return a list of {@link ApplicationExitInfo} records with the matching criteria, sorted in
     * the order from most recent to least recent.
     */
    ParceledListSlice<ApplicationExitInfo> getHistoricalProcessExitReasons(String packageName,
                                                                           int pid, int maxNum, int userId);

    /*
     * Kill the given PIDs, but the killing will be delayed until the device is idle
     * and the given process is imperceptible.
     */
    void killProcessesWhenImperceptible(int[] pids, String reason);

    /**
     * Set locus context for a given activity.
     *
     * @param activity
     * @param locusId  a unique, stable id that identifies this activity instance from others.
     * @param appToken ActivityRecord's appToken.
     */
    void setActivityLocusContext(ComponentName activity, LocusId locusId,
                                 IBinder appToken);

    void setProcessStateSummary(byte[] state);

    boolean isAppFreezerSupported();

    boolean isAppFreezerEnabled();

    void killUidForPermissionChange(int appId, int userId, String reason);

    void resetAppErrors();

    boolean enableAppFreezer(boolean enable);

    boolean enableFgsNotificationRateLimit(boolean enable);

    void holdLock(IBinder token, int durationMs);

    boolean stopProfile(int userId);

    ParceledListSlice queryIntentComponentsForIntentSender(IIntentSender sender, int matchFlags);

    int getUidProcessCapabilities(int uid, String callingPackage) throws RemoteException;

    void waitForBroadcastIdle() throws RemoteException;


    int getBackgroundRestrictionExemptionReason(int uid) throws RemoteException;


}