package com.john.freezeapp.usagestats;

import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.Build;
import android.os.RemoteException;

import com.john.freezeapp.FreezeAppManager;
import com.john.freezeapp.ThreadPool;
import com.john.freezeapp.client.ClientBinderManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class UsageStats {

    public interface Callback {
        void success(List<UsageStatsData> usageStatsDataList);

        void fail();
    }

    public static void requestUsageStatsData(Context context, Callback callback) {
        // FreezeAppManager.CacheAppModel appModel = FreezeAppManager.getAppModel(context, batteryUsageAppData.packageName);
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                try {
                    ParceledListSlice<android.app.usage.UsageStats> parceledListSlice = ClientBinderManager.getUsageStatsManager().queryUsageStats(UsageStatsManager.INTERVAL_DAILY, calendar.getTimeInMillis(), System.currentTimeMillis(), null, 0);
                    List<android.app.usage.UsageStats> list = parceledListSlice.getList();
                    List<UsageStatsData> usageStatsDatas = new ArrayList<>();
                    for (android.app.usage.UsageStats usageStats : list) {
                        UsageStatsData usageStatsData = new UsageStatsData();
                        usageStatsData.packageName = usageStats.getPackageName();
                        usageStatsData.firstTimeStamp = usageStats.getFirstTimeStamp();
                        usageStatsData.lastTimeStamp = usageStats.getLastTimeStamp();
                        usageStatsData.lastTimeUsed = usageStats.getLastTimeUsed();
                        usageStatsData.totalTimeInForeground = usageStats.getTotalTimeInForeground();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            usageStatsData.lastTimeVisible = usageStats.getLastTimeVisible();
                            usageStatsData.totalTimeVisible = usageStats.getTotalTimeVisible();
                            usageStatsData.lastTimeForegroundServiceUsed = usageStats.getLastTimeForegroundServiceUsed();
                            usageStatsData.totalTimeForegroundServiceUsed = usageStats.getTotalTimeForegroundServiceUsed();

                        }
                        // mLaunchCount , mAppLaunchCount

                        Object mLaunchCount = getReflectValue(android.app.usage.UsageStats.class, "mLaunchCount", usageStats);
                        if (mLaunchCount != null) {
                            try {
                                usageStatsData.launchCount = Integer.parseInt(String.valueOf(mLaunchCount));
                            } catch (Exception e) {
                                //
                            }
                        }

                        Object mAppLaunchCount = getReflectValue(android.app.usage.UsageStats.class, "mAppLaunchCount", usageStats);
                        if (mLaunchCount != null) {
                            try {
                                usageStatsData.appLaunchCount = Integer.parseInt(String.valueOf(mAppLaunchCount));
                            } catch (Exception e) {
                                //
                            }
                        }

                        usageStatsDatas.add(usageStatsData);
                    }
                    if (callback != null) {
                        callback.success(usageStatsDatas);
                    }

                } catch (RemoteException e) {
                    if (callback != null) {
                        callback.fail();
                    }
                }
            }
        });
    }


    private static Object getReflectValue(Class<?> clz, String fieldName, Object object) {
        try {
            Field declaredField = clz.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(object);
        } catch (Exception e) {
            return null;
        }
    }

}
