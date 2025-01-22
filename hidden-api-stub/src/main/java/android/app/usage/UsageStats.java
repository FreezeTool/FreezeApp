package android.app.usage;

import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseIntArray;

public final class UsageStats {

    public String mPackageName;

    public int mPackageToken = -1;

    public long mBeginTimeStamp;

    public long mEndTimeStamp;

    public long mLastTimeUsed;

    public long mLastTimeVisible;

    public long mTotalTimeInForeground;

    public long mTotalTimeVisible;

    public long mLastTimeForegroundServiceUsed;

    public long mTotalTimeForegroundServiceUsed;

    public long mLastTimeComponentUsed;

    public int mLaunchCount;

    public int mAppLaunchCount;

    public int mLastEvent;

    public SparseIntArray mActivities = new SparseIntArray();

    public ArrayMap<String, Integer> mForegroundServices = new ArrayMap<>();

    public ArrayMap<String, ArrayMap<String, Integer>> mChooserCounts = new ArrayMap<>();

    public SparseArray<SparseIntArray> mChooserCountsObfuscated = new SparseArray<>();

    public UsageStats() {
    }


    public UsageStats getObfuscatedForInstantApp() {
        throw new RuntimeException("STUB");
    }

    public String getPackageName() {
        throw new RuntimeException("STUB");
    }

    public long getFirstTimeStamp() {
        throw new RuntimeException("STUB");
    }

    public long getLastTimeStamp() {
        throw new RuntimeException("STUB");
    }


    public long getLastTimeUsed() {
        throw new RuntimeException("STUB");
    }

    /**
     * Get the last time this package's activity is visible in the UI, measured in milliseconds
     * since the epoch.
     */
    public long getLastTimeVisible() {
        throw new RuntimeException("STUB");
    }

    /**
     * Get the total time this package spent in the foreground, measured in milliseconds. When in
     * the foreground, the user is actively interacting with the app.
     */
    public long getTotalTimeInForeground() {
        throw new RuntimeException("STUB");
    }

    /**
     * Get the total time this package's activity is visible in the UI, measured in milliseconds.
     * Note: An app may be visible but not considered foreground. Apps in the foreground must be
     * visible, so visible time includes time in the foreground.
     */
    public long getTotalTimeVisible() {
        throw new RuntimeException("STUB");
    }

    /**
     * Get the last time this package's foreground service was used, measured in milliseconds since
     * the epoch.
     * <p/>
     * See {@link System#currentTimeMillis()}.
     */
    public long getLastTimeForegroundServiceUsed() {
        throw new RuntimeException("STUB");
    }

    /**
     * Get the total time this package's foreground services are started, measured in milliseconds.
     */
    public long getTotalTimeForegroundServiceUsed() {
        throw new RuntimeException("STUB");
    }

    public long getLastTimeAnyComponentUsed() {
        throw new RuntimeException("STUB");
    }

    public long getLastTimePackageUsed() {
        throw new RuntimeException("STUB");
    }

    public int getAppLaunchCount() {
        throw new RuntimeException("STUB");
    }

    private void mergeEventMap(SparseIntArray left, SparseIntArray right) {
        throw new RuntimeException("STUB");
    }

    private void mergeEventMap(ArrayMap<String, Integer> left, ArrayMap<String, Integer> right) {
        throw new RuntimeException("STUB");
    }

    public void add(UsageStats right) {
        throw new RuntimeException("STUB");
    }



    public void update(String className, long timeStamp, int eventType, int instanceId) {
        throw new RuntimeException("STUB");
    }
}