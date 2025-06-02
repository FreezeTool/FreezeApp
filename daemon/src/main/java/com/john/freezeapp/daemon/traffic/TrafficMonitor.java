package com.john.freezeapp.daemon.traffic;

import android.app.ActivityManagerHidden;
import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntentHidden;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.DataUsageRequest;
import android.net.INetworkStatsService;
import android.net.NetworkTemplate;
import android.net.netstats.IUsageCallback;
import android.os.Build;

import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.DaemonLog;
import com.john.freezeapp.daemon.DaemonService;
import com.john.freezeapp.daemon.util.DaemonUtil;
import com.john.freezeapp.util.CommonUtil;
import com.john.freezeapp.util.DeviceUtil;
import com.john.hidden.api.ReplaceRef;

import java.util.Arrays;

public class TrafficMonitor {

    private static IUsageCallbackWrapper sUsageCallbackWrapper;
    private static int notifyCount;

    public static void start(int threshold) {
        if (sUsageCallbackWrapper != null) {
            stop();
        }

        INetworkStatsService networkStatsService = DaemonService.getNetworkStatsService();
        if (networkStatsService == null) {
            return;
        }

        sUsageCallbackWrapper = new IUsageCallbackWrapper() {
            DataUsageRequest request;

            @Override
            public DataUsageRequest getRequest() {
                return request;
            }

            @Override
            public void onThresholdReached(DataUsageRequest request) {
                doTrafficMonitor(request);
            }

            @Override
            public void onCallbackReleased(DataUsageRequest request) {

            }

            @Override
            public void setRequest(DataUsageRequest finalRequest) {
                this.request = finalRequest;
            }
        };

        int matchRule = NetworkTemplate.MATCH_MOBILE;
        if (isNewUsageCallback()) {
            NetworkTemplate networkTemplate = new NetworkTemplate.Builder(matchRule).build();
            final DataUsageRequest request = new DataUsageRequest(DataUsageRequest.REQUEST_ID_UNSET,
                    networkTemplate, threshold);
            final DataUsageRequest finalRequest = networkStatsService.registerUsageCallback(DaemonUtil.getCallingPackageName(), request, new android.net.connectivity.android.net.netstats.IUsageCallback.Stub() {
                @Override
                public void onThresholdReached(DataUsageRequest request) {
                    sUsageCallbackWrapper.onThresholdReached(request);
                }

                @Override
                public void onCallbackReleased(DataUsageRequest request) {
                    sUsageCallbackWrapper.onCallbackReleased(request);
                }
            });
            sUsageCallbackWrapper.setRequest(finalRequest);
        } else if (DeviceUtil.atLeast33()) {
            NetworkTemplate networkTemplate = new NetworkTemplate.Builder(matchRule).build();
            final DataUsageRequest request = new DataUsageRequest(DataUsageRequest.REQUEST_ID_UNSET,
                    networkTemplate, threshold);
            networkStatsService.registerUsageCallback(DaemonUtil.getCallingPackageName(), request, new IUsageCallback.Stub() {
                @Override
                public void onThresholdReached(DataUsageRequest request) {
                    sUsageCallbackWrapper.onThresholdReached(request);
                }

                @Override
                public void onCallbackReleased(DataUsageRequest request) {
                    sUsageCallbackWrapper.onCallbackReleased(request);
                }
            });
        } else {
//                networkStatsService.registerUsageCallback()
        }


    }

    private static void doTrafficMonitor(DataUsageRequest request) {
        try {
            INotificationManager notificationManager = DaemonService.getNotificationManager();
            if (notificationManager != null) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel("trafficMonitor", "APP监控", NotificationManager.IMPORTANCE_LOW);
                    notificationChannel.setSound(null, null);
                    notificationChannel.setShowBadge(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        notificationChannel.setAllowBubbles(false);
                    }
                    notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{0, 500, 300, 500});
                    notificationChannel.setBypassDnd(true); // 突破勿扰模式
                    notificationManager.createNotificationChannels(DaemonUtil.getCallingPackageName(), new ParceledListSlice(Arrays.asList(notificationChannel)));
                }

                notifyCount++;
                Notification.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder = new Notification.Builder(Daemon.getDaemon().getContext(), "trafficMonitor");
                } else {
                    builder = new Notification.Builder(Daemon.getDaemon().getContext());
                }
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Notification.Builder notification = builder
                        .setContentTitle("流量监控")
                        .setContentText(String.format("超过流量阈值%s（%s次）", CommonUtil.getSizeText(request.thresholdInBytes), notifyCount))
                        .setSmallIcon(Icon.createWithBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setCategory(Notification.CATEGORY_CALL)
                        .setAutoCancel(true);

                if (DeviceUtil.atLeast33()) {
                    notification.setFullScreenIntent(ReplaceRef.<PendingIntent>unsafeCast(getPendingIntentActivity()), true);
                }
                notificationManager.enqueueNotificationWithTag(DaemonUtil.getCallingPackageName(), DaemonUtil.getCallingPackageName(), null, 123, notification.build(), 0);
            }
        } catch (Exception e) {
            DaemonLog.e(e, "doTrafficMonitor");
        }
    }

    private static PendingIntentHidden getPendingIntentActivity() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("com.john.freezeapp.traffic.monitor");
        IIntentSender intentSender = DaemonService.getActivityManager().getIntentSenderWithFeature(
                ActivityManagerHidden.INTENT_SENDER_ACTIVITY, DaemonUtil.getCallingPackageName(),
                null, null, null, 1, new Intent[]{intent},
                null,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE, null, 0);
        return new PendingIntentHidden(intentSender);
    }


    public static void stop() {
        INetworkStatsService networkStatsService = DaemonService.getNetworkStatsService();
        if (networkStatsService == null) {
            return;
        }

        if (sUsageCallbackWrapper != null) {
            notifyCount = 0;
            networkStatsService.unregisterUsageRequest(sUsageCallbackWrapper.getRequest());
            sUsageCallbackWrapper = null;
        }
    }

    /**
     * fix 部分手机的IUsageCallback包名有问题
     *
     * @return
     */
    public static boolean isNewUsageCallback() {
        try {
            Class.forName("android.net.connectivity.android.net.netstats.IUsageCallback");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isActive() {
        return sUsageCallbackWrapper != null;
    }


    private interface IUsageCallbackWrapper {

        DataUsageRequest getRequest();

        void onThresholdReached(DataUsageRequest request);

        void onCallbackReleased(DataUsageRequest request);

        void setRequest(DataUsageRequest finalRequest);
    }
}
