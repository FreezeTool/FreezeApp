package com.john.freezeapp.usagestats.appstandby;

import android.app.usage.AppStandbyInfo;
import android.content.pm.ParceledListSlice;

import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.util.DeviceUtil;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ThreadPool;

import java.util.ArrayList;
import java.util.List;

/**
 *  App待机分桶 https://developer.android.com/topic/performance/appstandby?hl=zh-cn
 */
public class AppStandby {

    public interface Callback {
        void success(List<AppStandbyData> appStandbyList);

        void fail();
    }

    public static boolean setAppStandbyBucket(String packageName, int bucket) {
        try {
            ClientSystemService.getUsageStatsManager().setAppStandbyBucket(packageName, bucket, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getAppStandbyBucket(String packageName) {
        try {
            return ClientSystemService.getUsageStatsManager().getAppStandbyBucket(packageName, null, 0);
        } catch (Exception e) {
            return -1;
        }
    }


    public static void requestAppStandbyBucket(Callback callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ParceledListSlice<AppStandbyInfo> appStandbyBuckets = ClientSystemService.getUsageStatsManager().getAppStandbyBuckets(null, 0);
                    if (appStandbyBuckets != null) {
                        List<AppStandbyInfo> list = appStandbyBuckets.getList();
                        List<AppStandbyData> appStandbyData = new ArrayList<>();
                        if (list != null && !list.isEmpty()) {
                            for (AppStandbyInfo appStandbyInfo : list) {
                                appStandbyData.add(new AppStandbyData(appStandbyInfo.mPackageName, appStandbyInfo.mStandbyBucket));
                            }
                        }
                        callback.success(appStandbyData);
                    } else {
                        callback.fail();
                    }

                } catch (Exception e) {
                    callback.fail();
                }
            }
        });
    }

    public static String getStandByBucketName(int standbyBucket) {
        for (StandbyBucket value : StandbyBucket.values()) {
            if (standbyBucket == value.getBucket()) {
                return value.getName();
            }
        }
        return "";
    }

    private static List<StandbyBucket> sAppStandbyBuckets = new ArrayList<>();

    static {
        sAppStandbyBuckets.add(StandbyBucket.STANDBY_BUCKET_ACTIVE);
        sAppStandbyBuckets.add(StandbyBucket.STANDBY_BUCKET_WORKING_SET);
        sAppStandbyBuckets.add(StandbyBucket.STANDBY_BUCKET_FREQUENT);
        sAppStandbyBuckets.add(StandbyBucket.STANDBY_BUCKET_RARE);
        if (DeviceUtil.atLeast30()) {
            sAppStandbyBuckets.add(StandbyBucket.STANDBY_BUCKET_RESTRICTED);
        }
        sAppStandbyBuckets.add(StandbyBucket.STANDBY_BUCKET_NEVER);

    }

    public static List<StandbyBucket> getStandbyBucket() {
        return sAppStandbyBuckets;
    }


}
