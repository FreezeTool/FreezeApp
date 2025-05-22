package com.john.freezeapp.usagestats.appstandby;

import android.app.usage.UsageStatsManager;
import android.app.usage.UsageStatsManagerHidden;

/**
 * public static String getStandByBucketName(int standbyBucket) {
 * if(standbyBucket == UsageStatsManagerHidden.STANDBY_BUCKET_EXEMPTED) {
 * return "豁免";
 * } else if(standbyBucket == UsageStatsManager.STANDBY_BUCKET_ACTIVE) {
 * return "活跃";
 * } else if(standbyBucket == UsageStatsManager.STANDBY_BUCKET_WORKING_SET) {
 * return "工作集";
 * } else if(standbyBucket == UsageStatsManager.STANDBY_BUCKET_FREQUENT) {
 * return "常用";
 * } else if(standbyBucket == UsageStatsManager.STANDBY_BUCKET_RARE) {
 * return "极少使用";
 * } else if(standbyBucket == UsageStatsManager.STANDBY_BUCKET_RESTRICTED) {
 * return "受限";
 * } else if(standbyBucket == UsageStatsManagerHidden.STANDBY_BUCKET_NEVER) {
 * return "无法使用";
 * }
 * return "";
 * }
 */
public enum StandbyBucket {
    STANDBY_BUCKET_EXEMPTED(UsageStatsManagerHidden.STANDBY_BUCKET_EXEMPTED, "豁免"),
    STANDBY_BUCKET_ACTIVE(UsageStatsManager.STANDBY_BUCKET_ACTIVE, "活跃"),
    STANDBY_BUCKET_WORKING_SET(UsageStatsManager.STANDBY_BUCKET_WORKING_SET, "工作集"),
    STANDBY_BUCKET_FREQUENT(UsageStatsManager.STANDBY_BUCKET_FREQUENT, "常用"),
    STANDBY_BUCKET_RARE(UsageStatsManager.STANDBY_BUCKET_RARE, "极少使用"),
    STANDBY_BUCKET_RESTRICTED(UsageStatsManager.STANDBY_BUCKET_RESTRICTED, "受限"),
    STANDBY_BUCKET_NEVER(UsageStatsManagerHidden.STANDBY_BUCKET_NEVER, "无法使用");

    private int bucket;
    private String name;

    StandbyBucket(int bucket, String name) {
        this.bucket = bucket;
        this.name = name;
    }


    public int getBucket() {
        return bucket;
    }

    public String getName() {
        return name;
    }
}
