package android.app.usage;


public final class AppStandbyInfo {

    public String mPackageName;
    public int mStandbyBucket;

    public AppStandbyInfo(String packageName, int bucket) {
        mPackageName = packageName;
        mStandbyBucket = bucket;
    }
}