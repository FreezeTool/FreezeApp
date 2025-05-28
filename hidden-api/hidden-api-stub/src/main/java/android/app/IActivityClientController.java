package android.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;

public interface IActivityClientController {
    void activityIdle(IBinder token, Configuration config, boolean stopProfiling);
    void activityResumed(IBinder token, boolean handleSplashScreenExit);
    void activityRefreshed(IBinder token);

    void activityTopResumedStateLost();

    void activityPaused(IBinder token);
    void activityStopped(IBinder token, Bundle state,
                         PersistableBundle persistentState, CharSequence description);
    void activityDestroyed(IBinder token);
    void activityLocalRelaunch(IBinder token);
    void activityRelaunched(IBinder token);

//    void reportSizeConfigurations(IBinder token,
//            SizeConfigurationBuckets sizeConfigurations);
    boolean moveActivityTaskToBack(IBinder token, boolean nonRoot);
    boolean shouldUpRecreateTask(IBinder token, String destAffinity);
    boolean navigateUpTo(IBinder token, Intent target, String resolvedType,
                         int resultCode, Intent resultData);
    boolean releaseActivityInstance(IBinder token);
    boolean finishActivity(IBinder token, int code, Intent data, int finishTask);
    boolean finishActivityAffinity(IBinder token);
    /** Finish all activities that were started for result from the specified activity. */
    void finishSubActivity(IBinder token, String resultWho, int requestCode);
    /**
     * Indicates that when the activity finsihes, the result should be immediately sent to the
     * originating activity. Must only be invoked during MediaProjection setup.
     */
    void setForceSendResultForMediaProjection(IBinder token);

    boolean isTopOfTask(IBinder token);
    boolean willActivityBeVisible(IBinder token);
    int getDisplayId(IBinder activityToken);
    int getTaskForActivity(IBinder token, boolean onlyRoot);
    /**
     * Returns the {@link Configuration} of the task which hosts the Activity, or {@code null} if
     * the task {@link Configuration} cannot be obtained.
     */
    Configuration getTaskConfiguration(IBinder activityToken);
    IBinder getActivityTokenBelow(IBinder token);
    ComponentName getCallingActivity(IBinder token);
    String getCallingPackage(IBinder token);
    int getLaunchedFromUid(IBinder token);
    String getLaunchedFromPackage(IBinder token);

    void setRequestedOrientation(IBinder token, int requestedOrientation);
    int getRequestedOrientation(IBinder token);

    boolean convertFromTranslucent(IBinder token);
    boolean convertToTranslucent(IBinder token, Bundle options);

    boolean isImmersive(IBinder token);
    void setImmersive(IBinder token, boolean immersive);

    boolean enterPictureInPictureMode(IBinder token, PictureInPictureParams params);
    void setPictureInPictureParams(IBinder token, PictureInPictureParams params);
    void setShouldDockBigOverlays(IBinder token, boolean shouldDockBigOverlays);
    void toggleFreeformWindowingMode(IBinder token);

    void startLockTaskModeByToken(IBinder token);
    void stopLockTaskModeByToken(IBinder token);
    void showLockTaskEscapeMessage(IBinder token);
    void setTaskDescription(IBinder token, ActivityManager.TaskDescription values);

    boolean showAssistFromActivity(IBinder token, Bundle args);
    boolean isRootVoiceInteraction(IBinder token);
    void startLocalVoiceInteraction(IBinder token, Bundle options);
    void stopLocalVoiceInteraction(IBinder token);

    void setShowWhenLocked(IBinder token, boolean showWhenLocked);
    void setInheritShowWhenLocked(IBinder token, boolean setInheritShownWhenLocked);
    void setTurnScreenOn(IBinder token, boolean turnScreenOn);
    void reportActivityFullyDrawn(IBinder token, boolean restoredFromBundle);
    /**
     * Overrides the animation of activity pending transition. This call is not one-way because
     * the method is usually used after startActivity or Activity#finish. If this is non-blocking,
     * the calling activity may proceed to complete pause and become stopping state, which will
     * cause the request to be ignored. Besides, startActivity and Activity#finish are blocking
     * calls, so this method should be the same as them to keep the invocation order.
     */
    void overridePendingTransition(IBinder token, String packageName,
            int enterAnim, int exitAnim, int backgroundColor);
    int setVrMode(IBinder token, boolean enabled, ComponentName packageName);

    /** See {@link android.app.Activity#setRecentsScreenshotEnabled}. */
    void setRecentsScreenshotEnabled(IBinder token, boolean enabled);

    /**
     * It should only be called from home activity to remove its outdated snapshot. The home
     * snapshot is used to speed up entering home from screen off. If the content of home activity
     * is significantly different from before taking the snapshot, then the home activity can use
     * this method to avoid inconsistent transition.
     */
    void invalidateHomeTaskSnapshot(IBinder homeToken);

//    void dismissKeyguard(IBinder token, IKeyguardDismissCallback callback,
//            CharSequence message);

    /** Registers remote animations for a specific activity. */
//    void registerRemoteAnimations(IBinder token, RemoteAnimationDefinition definition);

    /** Unregisters all remote animations for a specific activity. */
    void unregisterRemoteAnimations(IBinder token);

    /**
     * Reports that an Activity received a back key press.
//     */
//    void onBackPressed(IBinder activityToken,
//            IRequestFinishCallback callback);

    /** Reports that the splash screen view has attached to activity.  */
    void splashScreenAttached(IBinder token);

    /**
     * Shows or hides a Camera app compat toggle for stretched issues with the requested state.
     *
     * @param token The token for the window that needs a control.
     * @param showControl Whether the control should be shown or hidden.
     * @param transformationApplied Whether the treatment is already applied.
     * @param callback The callback executed when the user clicks on a control.
     */
//    void requestCompatCameraControl(IBinder token, boolean showControl,
//            boolean transformationApplied, ICompatCameraControlCallback callback);
}