package com.john.freezeapp.monitor;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.ITaskStackListener;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.window.TaskSnapshot;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class AppMonitorManager {


    /**
     * TaskInfo{userId=0 taskId=857 displayId=0 isRunning=true baseIntent=Intent
     * { act=android.intent.action.MAIN cat=[android.intent.category.HOME]
     * flg=0x10000100 cmp=com.google.android.apps.nexuslauncher/.NexusLauncherActivity }
     * baseActivity=ComponentInfo{com.google.android.googlequicksearchbox/com.google.android.apps.search.googleapp.launcher.minusone.activity.MinusOneActivity}
     * topActivity=ComponentInfo{com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity}
     * origActivity=null
     * realActivity=ComponentInfo{com.google.android.apps.nexuslauncher/com.google.android.apps.nexuslauncher.NexusLauncherActivity}
     * numActivities=2
     * lastActiveTime=119435434
     * supportsMultiWindow=true
     * resizeMode=2
     * isResizeable=true
     * minWidth=-1
     * minHeight=-1
     * defaultMinSize=220
     * token=WCT{android.window.IWindowContainerToken$Stub$Proxy@1bd82c9}
     * topActivityType=2
     * pictureInPictureParams=null
     * shouldDockBigOverlays=false
     * launchIntoPipHostTaskId=-1
     * lastParentTaskIdBeforePip=-1
     * displayCutoutSafeInsets=Rect(0, 128 - 0, 0)
     * topActivityInfo=ActivityInfo{c001dce com.google.android.apps.nexuslauncher.NexusLauncherActivity}
     * launchCookies=[]
     * positionInParent=Point(0, 0)
     * parentTaskId=-1
     * isFocused=false
     * isVisible=false
     * isVisibleRequested=false isSleeping=false locusId=LocusId[17_chars]
     * displayAreaFeatureId=1
     * isTopActivityTransparent=false
     * appCompatTaskInfo=AppCompatTaskInfo {
     * topActivityInSizeCompat=false
     * topActivityEligibleForLetterboxEducation= false
     * isLetterboxEducationEnabled= false
     * isLetterboxDoubleTapEnabled= false
     * topActivityEligibleForUserAspectRatioButton= false
     * topActivityBoundsLetterboxed= false
     * isFromLetterboxDoubleTap= false
     * topActivityLetterboxVerticalPosition= -1
     * topActivityLetterboxHorizontalPosition= -1
     * topActivityLetterboxWidth=1080
     * topActivityLetterboxHeight=2400
     * isUserFullscreenOverrideEnabled=false
     * isSystemFullscreenOverrideEnabled=false
     * cameraCompatTaskInfo=CameraCompatTaskInfo { cameraCompatControlState=hidden freeformCameraCompatMode=inactive}}}
     */
    private static ITaskStackListener.Stub sTaskStackListener = null;

    private static void onTaskRunning(ActivityManager.RunningTaskInfo runningTaskInfo) {
        for (IAppMonitor sAppMonitor : sAppMonitors) {
            sAppMonitor.show(runningTaskInfo);
        }
    }


    public static void registerTaskStackListener() {
        if (!ClientBinderManager.isActive()) {
            return;
        }

        IActivityManager activityManager = ClientBinderManager.getActivityManager();
        if (activityManager != null) {
            try {

                if (sTaskStackListener != null) {
                    activityManager.unregisterTaskStackListener(sTaskStackListener);
                }

                sTaskStackListener = new ITaskStackListener.Stub() {
                    @Override
                    public void onTaskStackChanged() {
                        ClientLog.log("AppMonitorManager onTaskStackChanged");
                    }

                    @Override
                    public void onActivityPinned(String packageName, int userId, int taskId, int stackId) {
                        ClientLog.log(String.format("AppMonitorManager onTaskStackChanged packageName=%s,userId=%d,taskId=%d,stackId=%d", packageName, userId, taskId, stackId));
                    }

                    @Override
                    public void onActivityUnpinned() {
                        ClientLog.log("AppMonitorManager onActivityUnpinned");
                    }

                    @Override
                    public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo task, boolean homeTaskVisible, boolean clearedTask, boolean wasVisible) {
                        ClientLog.log(String.format("AppMonitorManager onActivityRestartAttempt task=%s,userId=%b,taskId=%b,stackId=%b", task.toString(), homeTaskVisible, clearedTask, wasVisible));

                    }

                    @Override
                    public void onActivityForcedResizable(String packageName, int taskId, int reason) {
                        ClientLog.log(String.format("AppMonitorManager onActivityForcedResizable packageName=%s,taskId=%d,reason=%d", packageName, taskId, reason));
                    }

                    @Override
                    public void onActivityDismissingDockedTask() {
                        ClientLog.log("AppMonitorManager onActivityDismissingDockedTask");
                    }

                    @Override
                    public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo taskInfo, int requestedDisplayId) {
                        ClientLog.log(String.format("AppMonitorManager onActivityLaunchOnSecondaryDisplayFailed taskInfo=%s,userId=%d", taskInfo.toString(), requestedDisplayId));

                    }

                    @Override
                    public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo taskInfo, int requestedDisplayId) {
                        ClientLog.log(String.format("AppMonitorManager onActivityLaunchOnSecondaryDisplayRerouted taskInfo=%s,userId=%d", taskInfo.toString(), requestedDisplayId));

                    }

                    @Override
                    public void onTaskCreated(int taskId, ComponentName componentName) {
                        ClientLog.log(String.format("AppMonitorManager onTaskCreated taskId=%d", taskId));
                    }

                    @Override
                    public void onTaskRemoved(int taskId) {
                        ClientLog.log(String.format("AppMonitorManager onTaskRemoved taskId=%d", taskId));
                    }

                    @Override
                    public void onTaskMovedToFront(ActivityManager.RunningTaskInfo taskInfo) {
                        ClientLog.log(String.format("AppMonitorManager onTaskMovedToFront taskInfo=%s", taskInfo.toString()));
                        ComponentName topActivity = taskInfo.topActivity;
                        if (topActivity != null) {
                            String className = topActivity.getClassName();
                            ClientLog.log(String.format("AppMonitorManager topActivityRecord=%s", className));
                        }
                        onTaskRunning(taskInfo);
                    }

                    @Override
                    public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo taskInfo) {
                        ClientLog.log(String.format("AppMonitorManager onTaskDescriptionChanged taskInfo=%s", taskInfo.toString()));
                        ComponentName topActivity = taskInfo.topActivity;
                        if (topActivity != null) {
                            String className = topActivity.getClassName();
                            ClientLog.log(String.format("AppMonitorManager topActivityRecord=%s", className));
                        }

                        onTaskRunning(taskInfo);

                    }

                    @Override
                    public void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) {
                        ClientLog.log(String.format("AppMonitorManager onActivityRequestedOrientationChanged taskId=%d,requestedOrientation=%d", taskId, requestedOrientation));
                    }

                    @Override
                    public void onTaskRemovalStarted(ActivityManager.RunningTaskInfo taskInfo) {
                        ClientLog.log(String.format("AppMonitorManager onTaskRemovalStarted taskInfo=%s", taskInfo.toString()));
                    }

                    @Override
                    public void onTaskProfileLocked(ActivityManager.RunningTaskInfo taskInfo) {
                        ClientLog.log(String.format("AppMonitorManager onTaskProfileLocked taskInfo=%s", taskInfo.toString()));
                    }

                    @Override
                    public void onTaskSnapshotChanged(int taskId, TaskSnapshot snapshot) {
                        ClientLog.log(String.format("AppMonitorManager onTaskSnapshotChanged taskId=%d,snapshot=%s", taskId, snapshot.toString()));
                    }

                    @Override
                    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo taskInfo) {
                        ClientLog.log(String.format("AppMonitorManager onBackPressedOnTaskRoot taskInfo=%s", taskInfo.toString()));
                    }

                    @Override
                    public void onTaskDisplayChanged(int taskId, int newDisplayId) {
                        ClientLog.log(String.format("AppMonitorManager onTaskSnapshotChanged taskId=%d,newDisplayId=%d", taskId, newDisplayId));
                    }

                    @Override
                    public void onRecentTaskListUpdated() {
                        ClientLog.log("AppMonitorManager onRecentTaskListUpdated");
                    }

                    @Override
                    public void onRecentTaskListFrozenChanged(boolean frozen) {
                        ClientLog.log(String.format("AppMonitorManager onRecentTaskListFrozenChanged frozen=%b", frozen));
                    }

                    @Override
                    public void onTaskFocusChanged(int taskId, boolean focused) {
                        ClientLog.log(String.format("AppMonitorManager onTaskFocusChanged taskId=%d,newDisplayId=%b", taskId, focused));
                    }

                    @Override
                    public void onTaskRequestedOrientationChanged(int taskId, int requestedOrientation) {
                        ClientLog.log(String.format("AppMonitorManager onTaskRequestedOrientationChanged taskId=%d,newDisplayId=%d", taskId, requestedOrientation));
                    }

                    @Override
                    public void onActivityRotation(int displayId) {
                        ClientLog.log(String.format("AppMonitorManager onActivityRotation displayId=%d", displayId));
                    }

                    @Override
                    public void onTaskMovedToBack(ActivityManager.RunningTaskInfo taskInfo) {
                        ClientLog.log(String.format("AppMonitorManager onTaskMovedToBack taskInfo=%s", taskInfo.toString()));
                    }

                    @Override
                    public void onLockTaskModeChanged(int mode) {
                        ClientLog.log(String.format("AppMonitorManager onLockTaskModeChanged displayId=%d", mode));
                    }
                };
                activityManager.registerTaskStackListener(sTaskStackListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static final List<IAppMonitor> sAppMonitors = new ArrayList<>();

    public static void registerAppMonitor(IAppMonitor appMonitor) {
        if (sAppMonitors.contains(appMonitor)) {
            return;
        }
        sAppMonitors.add(appMonitor);
    }

    public static void unregisterAppMonitor(IAppMonitor appMonitor) {
        sAppMonitors.remove(appMonitor);
    }

    public interface IAppMonitor {
        void show(ActivityManager.RunningTaskInfo runningTaskInfo);
    }

    public static boolean isAppMonitor() {
        return SharedPrefUtil.getBoolean(SharedPrefUtil.KEY_APP_MONITOR_SWITCHER, false);
    }

    public static void setAppMonitor(boolean switcher) {
        SharedPrefUtil.setBoolean(SharedPrefUtil.KEY_APP_MONITOR_SWITCHER, switcher);
    }

    public static void startAppMonitor(Context context) {
        if (isAppMonitor() && FreezeUtil.isOverlayPermission(context)) {
            AppMonitorService.startAppMonitor(context);
        }
    }

    public static void stopAppMonitor(Context context) {
        AppMonitorService.stopAppMonitor(context);
    }
}
