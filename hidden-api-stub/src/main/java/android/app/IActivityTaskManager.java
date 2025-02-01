package android.app;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.service.voice.IVoiceInteractionSession;
import android.view.IRecentsAnimationRunner;
import android.window.IWindowOrganizerController;

import com.android.internal.app.IVoiceInteractor;

import java.util.List;

public interface IActivityTaskManager {
    int startActivity(IApplicationThread caller, String callingPackage,
                      String callingFeatureId, Intent intent, String resolvedType,
                      IBinder resultTo, String resultWho, int requestCode,
                      int flags, ProfilerInfo profilerInfo, Bundle options);

    int startActivities(IApplicationThread caller, String callingPackage,
                        String callingFeatureId, Intent[] intents, String[] resolvedTypes,
                        IBinder resultTo, Bundle options, int userId);

    int startActivityAsUser(IApplicationThread caller, String callingPackage,
                            String callingFeatureId, Intent intent, String resolvedType,
                            IBinder resultTo, String resultWho, int requestCode, int flags,
                            ProfilerInfo profilerInfo, Bundle options, int userId);

    boolean startNextMatchingActivity(IBinder callingActivity,
                                      Intent intent, Bundle options);

    /**
     * The DreamActivity has to be started a special way that does not involve the PackageParser.
     * The DreamActivity is a framework component inserted the dream application process. Hence,
     * it is not declared the application's manifest and cannot be parsed. startDreamActivity
     * creates the activity and starts it without reaching out to the PackageParser.
     */
    boolean startDreamActivity(Intent intent);

    int startActivityIntentSender(IApplicationThread caller,
                                  IIntentSender target, IBinder whitelistToken, Intent fillInIntent,
                                  String resolvedType, IBinder resultTo, String resultWho, int requestCode,
                                  int flagsMask, int flagsValues, Bundle options);

//    WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage,
//                                    String callingFeatureId, Intent intent, String resolvedType,
//                                    IBinder resultTo, String resultWho, int requestCode, int flags,
//                                    ProfilerInfo profilerInfo, Bundle options, int userId);

    int startActivityWithConfig(IApplicationThread caller, String callingPackage,
                                String callingFeatureId, Intent intent, String resolvedType,
                                IBinder resultTo, String resultWho, int requestCode, int startFlags,
                                Configuration newConfig, Bundle options, int userId);

    int startVoiceActivity(String callingPackage, String callingFeatureId, int callingPid,
                           int callingUid, Intent intent, String resolvedType,
                           IVoiceInteractionSession session, IVoiceInteractor interactor, int flags,
                           ProfilerInfo profilerInfo, Bundle options, int userId);

    String getVoiceInteractorPackageName(IBinder callingVoiceInteractor);

    int startAssistantActivity(String callingPackage, String callingFeatureId, int callingPid,
                               int callingUid, Intent intent, String resolvedType, Bundle options, int userId);

    int startActivityFromGameSession(IApplicationThread caller, String callingPackage,
                                     String callingFeatureId, int callingPid, int callingUid, Intent intent,
                                     int taskId, int userId);

    void startRecentsActivity(Intent intent, long eventTime,
                              IRecentsAnimationRunner recentsAnimationRunner);

    int startActivityFromRecents(int taskId, Bundle options);

    int startActivityAsCaller(IApplicationThread caller, String callingPackage,
                              Intent intent, String resolvedType, IBinder resultTo, String resultWho,
                              int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options,
                              boolean ignoreTargetSecurity, int userId);

    boolean isActivityStartAllowedOnDisplay(int displayId, Intent intent, String resolvedType,
                                            int userId);

    void unhandledBack();

    /**
     * Returns an interface to control the activity related operations.
     */
    IActivityClientController getActivityClientController();

    int getFrontActivityScreenCompatMode();

    void setFrontActivityScreenCompatMode(int mode);

    void setFocusedTask(int taskId);

    boolean removeTask(int taskId);

    void removeAllVisibleRecentTasks();

    List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents,
                                                   boolean keepIntentExtra, int displayId);

    void moveTaskToFront(IApplicationThread app, String callingPackage, int task,
                         int flags, Bundle options);

    ParceledListSlice<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags,
                                                                     int userId);

    boolean isTopActivityImmersive();

    ActivityManager.TaskDescription getTaskDescription(int taskId);

    void reportAssistContextExtras(IBinder assistToken, Bundle extras,
                                   AssistStructure structure, AssistContent content, Uri referrer);

    void setFocusedRootTask(int taskId);

    ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo();

    Rect getTaskBounds(int taskId);

    void cancelRecentsAnimation(boolean restoreHomeRootTaskPosition);

    void updateLockTaskPackages(int userId, String[] packages);

    boolean isInLockTaskMode();

    int getLockTaskModeState();

    List<IBinder> getAppTasks(String callingPackage);

    void startSystemLockTaskMode(int taskId);

    void stopSystemLockTaskMode();

    void finishVoiceTask(IVoiceInteractionSession session);

    int addAppTask(IBinder activityToken, Intent intent,
                   ActivityManager.TaskDescription description, Bitmap thumbnail);

    Point getAppTaskThumbnailSize();

    void releaseSomeActivities(IApplicationThread app);

    Bitmap getTaskDescriptionIcon(String filename, int userId);

    void registerTaskStackListener(ITaskStackListener listener);

    void unregisterTaskStackListener(ITaskStackListener listener);

    void setTaskResizeable(int taskId, int resizeableMode);

    /**
     * Resize the task with given bounds
     *
     * @param taskId     The id of the task to set the bounds for.
     * @param bounds     The new bounds.
     * @param resizeMode Resize mode defined as {@code ActivityTaskManager#RESIZE_MODE_*} constants.
     * @return Return true on success. Otherwise false.
     */
    boolean resizeTask(int taskId, Rect bounds, int resizeMode);

    void moveRootTaskToDisplay(int taskId, int displayId);

    void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop);

    /**
     * Removes root tasks the input windowing modes from the system if they are of activity type
     * ACTIVITY_TYPE_STANDARD or ACTIVITY_TYPE_UNDEFINED
     */
    void removeRootTasksInWindowingModes(int[] windowingModes);

    /**
     * Removes root tasks of the activity types from the system.
     */
    void removeRootTasksWithActivityTypes(int[] activityTypes);

    List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos();

    ActivityTaskManager.RootTaskInfo getRootTaskInfo(int windowingMode, int activityType);

    List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfosOnDisplay(int displayId);

    ActivityTaskManager.RootTaskInfo getRootTaskInfoOnDisplay(int windowingMode, int activityType, int displayId);

    /**
     * Informs ActivityTaskManagerService that the keyguard is showing.
     *
     * @param showingKeyguard True if the keyguard is showing, false otherwise.
     * @param showingAod      True if AOD is showing, false otherwise.
     */
    void setLockScreenShown(boolean showingKeyguard, boolean showingAod);

    Bundle getAssistContextExtras(int requestType);

    boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver,
                                       Bundle receiverExtras, IBinder activityToken,
                                       boolean focused, boolean newSessionId);

    boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras,
                                IBinder activityToken, int flags);

    boolean isAssistDataAllowedOnCurrentActivity();

    boolean requestAssistDataForTask(IAssistDataReceiver receiver, int taskId,
                                     String callingPackageName);

    void keyguardGoingAway(int flags);

    void suppressResizeConfigChanges(boolean suppress);

    /**
     * Returns an interface enabling the management of window organizers.
     */
    IWindowOrganizerController getWindowOrganizerController();

    /**
     * Sets whether we are currently an interactive split screen resize operation where we
     * are changing the docked stack size.
     */
    void setSplitScreenResizing(boolean resizing);

    boolean supportsLocalVoiceInteraction();

    // Get device configuration
    ConfigurationInfo getDeviceConfigurationInfo();

    /**
     * Cancels the window transitions for the given task.
     */
    void cancelTaskWindowTransition(int taskId);

    /**
     * @param taskId               the id of the task to retrieve the sAutoapshots for
     * @param isLowResolution      if set, if the snapshot needs to be loaded from disk, this will load
     *                             a reduced resolution of it, which is much faster
     * @param takeSnapshotIfNeeded if set, call {@link #takeTaskSnapshot} to trigger the snapshot
     *                             if no cache exists.
     * @return a graphic buffer representing a screenshot of a task
     */
    android.window.TaskSnapshot getTaskSnapshot(
            int taskId, boolean isLowResolution, boolean takeSnapshotIfNeeded);

    /**
     * @param taskId the id of the task to take a snapshot of
     * @return a graphic buffer representing a screenshot of a task
     */
    android.window.TaskSnapshot takeTaskSnapshot(int taskId);

    /**
     * Return the user id of last resumed activity.
     */
    int getLastResumedActivityUserId();


    boolean updateConfiguration(Configuration values);

    void updateLockTaskFeatures(int userId, int flags);

//    /**
//     * Registers a remote animation to be run for all activity starts from a certapackage during
//     * a short predefined amount of time.
//     */
//    void registerRemoteAnimationForNextActivityStart(String packageName,
//                                                     RemoteAnimationAdapter adapter, IBinder launchCookie);
//
//    /**
//     * Registers remote animations for a display.
//     */
//    void registerRemoteAnimationsForDisplay(int displayId, RemoteAnimationDefinition definition);
//
    void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity);

    void setVrThread(int tid);

    void setPersistentVrThread(int tid);

    void stopAppSwitches();

    void resumeAppSwitches();

    void setActivityController(IActivityController watcher, boolean imAMonkey);

    void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake);

    int getPackageScreenCompatMode(String packageName);

    void setPackageScreenCompatMode(String packageName, int mode);

    boolean getPackageAskScreenCompat(String packageName);

    void setPackageAskScreenCompat(String packageName, boolean ask);

    /**
     * Clears launch params for given packages.
     */
    void clearLaunchParamsForPackages(List<String> packageNames);

//    /**
//     * A splash screen view has copied.
//     */
//    void onSplashScreenViewCopyFinished(int taskId,
//                                        SplashScreenView.SplashScreenViewParcelable material);

    /**
     * When the Picture-in-picture state has changed.
     */
    void onPictureInPictureStateChanged(PictureInPictureUiState pipState);

    /**
     * Re-attach navbar to the display during a recents transition.
     * TODO(188595497): Remove this once navbar attachment is shell.
     */
    void detachNavigationBarFromApp(IBinder transition);

    /**
     * Marks a process as a delegate for the currently playing remote transition animation. This
     * must be called from a process that is already a remote transition player or delegate. Any
     * marked delegates are cleaned-up automatically at the end of the transition.
     *
     * @param caller is the IApplicationThread representing the calling process.
     */
    void setRunningRemoteTransitionDelegate(IApplicationThread caller);

    /**
//     * Prepare the back navigation the server. This setups the leashed for sysui to animate
//     * the back gesture and returns the data needed for the animation.
//     *
//     * @param requestAnimation true if the caller wishes to animate the back navigation
//     * @param focusObserver    a remote callback to nofify shell when the focused window lost focus.
//     */
//    android.window.BackNavigationInfo startBackNavigation(boolean requestAnimation,
//                                                          IWindowFocusObserver focusObserver, BackAnimationAdaptor adaptor);
}