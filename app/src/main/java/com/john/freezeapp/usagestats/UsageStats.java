package com.john.freezeapp.usagestats;

import android.app.usage.UsageStatsHidden;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.RemoteException;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ThreadPool;
import com.john.hidden.api.ReplaceRef;

import java.util.ArrayList;
import java.util.Calendar;
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

                try {
                    if (callback != null) {
                        callback.success(getUsageStatsData());
                    }
                } catch (RemoteException e) {
                    if (callback != null) {
                        callback.fail();
                    }
                }
            }
        });
    }

    public static List<UsageStatsData> getUsageStatsData() throws RemoteException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        ParceledListSlice<android.app.usage.UsageStats> parceledListSlice = ClientSystemService.getUsageStatsManager().queryUsageStats(UsageStatsManager.INTERVAL_DAILY, calendar.getTimeInMillis(), System.currentTimeMillis(), null, 0);
        List<android.app.usage.UsageStats> list = parceledListSlice.getList();
        List<UsageStatsData> usageStatsDatas = new ArrayList<>();
        for (android.app.usage.UsageStats usageStats : list) {
            UsageStatsData usageStatsData = new UsageStatsData();
            usageStatsData.packageName = usageStats.getPackageName();
            usageStatsData.firstTimeStamp = usageStats.getFirstTimeStamp();
            usageStatsData.lastTimeStamp = usageStats.getLastTimeStamp();
            usageStatsData.lastTimeUsed = usageStats.getLastTimeUsed();
            usageStatsData.totalTimeInForeground = usageStats.getTotalTimeInForeground();
            if (FreezeUtil.atLeast29()) {
                usageStatsData.lastTimeVisible = usageStats.getLastTimeVisible();
                usageStatsData.totalTimeVisible = usageStats.getTotalTimeVisible();
                usageStatsData.lastTimeForegroundServiceUsed = usageStats.getLastTimeForegroundServiceUsed();
                usageStatsData.totalTimeForegroundServiceUsed = usageStats.getTotalTimeForegroundServiceUsed();

            }
            usageStatsData.launchCount = ReplaceRef.<UsageStatsHidden>unsafeCast(usageStats).mLaunchCount;
            if (FreezeUtil.atLeast28()) {
                usageStatsData.appLaunchCount = ReplaceRef.<UsageStatsHidden>unsafeCast(usageStats).mAppLaunchCount;
            }
            usageStatsDatas.add(usageStatsData);
        }
        return usageStatsDatas;
    }


}
