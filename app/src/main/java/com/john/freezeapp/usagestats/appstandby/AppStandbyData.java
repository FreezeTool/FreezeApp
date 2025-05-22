package com.john.freezeapp.usagestats.appstandby;

public class AppStandbyData implements Comparable<AppStandbyData>{
    public String packageName;
    public int standbyBucket;

    public AppStandbyData(String mPackageName, int mStandbyBucket) {
        this.packageName = mPackageName;
        this.standbyBucket = mStandbyBucket;
    }

    @Override
    public int compareTo(AppStandbyData o) {
        return Long.compare(standbyBucket, o.standbyBucket);
    }
}
