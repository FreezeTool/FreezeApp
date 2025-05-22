package com.john.freezeapp.usagestats.appstandby;

public class AppStandbyData {
    public String packageName;
    public int standbyBucket;

    public AppStandbyData(String mPackageName, int mStandbyBucket) {
        this.packageName = mPackageName;
        this.standbyBucket = mStandbyBucket;
    }
}
