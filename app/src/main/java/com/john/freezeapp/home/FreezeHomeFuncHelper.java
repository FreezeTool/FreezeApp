package com.john.freezeapp.home;

import android.app.smartspace.ISmartspaceManager;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceSessionId;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.app.smartspace.uitemplatedata.BaseTemplateData;
import android.app.smartspace.uitemplatedata.Text;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Build;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.View;

import com.android.internal.app.IBatteryStats;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.CommandActivity;
import com.john.freezeapp.MainActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.battery.BatteryUsageActivity;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.freeze.ManagerActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FreezeHomeFuncHelper {
    public static List<FreezeHomeFuncData> getFreezeHomeFuncData(Context context) {
        if (!ClientBinderManager.isActive()) {
            return null;
        }

        List<FreezeHomeFuncData> list = new ArrayList<>();
        list.add(new FreezeHomeFuncData(context.getResources().getString(R.string.main_manager_app), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManagerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }));
        list.add(new FreezeHomeFuncData(context.getResources().getString(R.string.main_command_app), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommandActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }));
        IBatteryStats batteryStats = ClientBinderManager.getBatteryStats();
        if (batteryStats != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list.add(new FreezeHomeFuncData(context.getResources().getString(R.string.main_battery_usage), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BatteryUsageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }));
        }
        if (BuildConfig.DEBUG) {
            list.add(new FreezeHomeFuncData(context.getResources().getString(R.string.main_test), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toTest2(context);
                }
            }));
        }

        return list;
    }

    /**
     * smartspace
     *
     * @param context
     */
    private static void toTest2(Context context) {
        try {
            ISmartspaceManager smartspaceManager = ClientBinderManager.getSmartspaceManager();
            if (smartspaceManager != null) {
                UserHandle userHandle = UserHandle.getUserHandleForUid(Process.myUid());
                SmartspaceSessionId sessionId = new SmartspaceSessionId(
                        context.getPackageName() + ":" + UUID.randomUUID().toString(), userHandle);
                SmartspaceConfig smartspaceConfig = new SmartspaceConfig.Builder(context, "lockscreen").build();

                smartspaceManager.createSmartspaceSession(smartspaceConfig, sessionId, new Binder());

                BaseTemplateData baseTemplateData = new BaseTemplateData.Builder(SmartspaceTarget.UI_TEMPLATE_SUB_CARD)
                        .setSubtitleItem(new BaseTemplateData.SubItemInfo.Builder()
                                .setText(new Text.Builder("John").build())
                                .build())
                        .build();
                SmartspaceTarget smartspaceTarget = new SmartspaceTarget.Builder(BuildConfig.APPLICATION_ID, new ComponentName(BuildConfig.APPLICATION_ID, MainActivity.class.getName()), userHandle)
                        .setTemplateData(baseTemplateData)
                        .build();
                SmartspaceTargetEvent smartspaceTargetEvent = new SmartspaceTargetEvent.Builder(SmartspaceTargetEvent.EVENT_UI_SURFACE_SHOWN)
                        .setSmartspaceTarget(smartspaceTarget).build();

                smartspaceManager.notifySmartspaceEvent(sessionId, smartspaceTargetEvent);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * usagestats
     *
     * @param context
     */
    private static void toTest(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        try {
            ParceledListSlice<UsageStats> parceledListSlice = ClientBinderManager.getUsageStatsManager().queryUsageStats(UsageStatsManager.INTERVAL_DAILY, calendar.getTimeInMillis(), System.currentTimeMillis(), null, 0);
            List<UsageStats> list = parceledListSlice.getList();

            for (UsageStats usageStats : list) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(usageStats.getPackageName());
                stringBuilder.append(",");
//                stringBuilder.append(usageStats.mLaunchCount);

                ClientLog.log(stringBuilder.toString());
            }


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
