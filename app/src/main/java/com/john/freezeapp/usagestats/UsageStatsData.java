package com.john.freezeapp.usagestats;

public class UsageStatsData implements Comparable<UsageStatsData> {

    public String packageName;
    public long firstTimeStamp;
    public long lastTimeStamp;
    public long lastTimeUsed;
    public long totalTimeInForeground;
    public long lastTimeVisible;
    public long totalTimeVisible;
    public long lastTimeForegroundServiceUsed;
    public long totalTimeForegroundServiceUsed;
    public int launchCount;
    public int appLaunchCount;
    public boolean isExpand;

    @Override
    public String toString() {
        return "UsageStatsData{" +
                "appLaunchCount=" + appLaunchCount +
                ", packageName='" + packageName + '\'' +
                ", firstTimeStamp=" + firstTimeStamp +
                ", lastTimeStamp=" + lastTimeStamp +
                ", lastTimeUsed=" + lastTimeUsed +
                ", totalTimeInForeground=" + totalTimeInForeground +
                ", lastTimeVisible=" + lastTimeVisible +
                ", totalTimeVisible=" + totalTimeVisible +
                ", lastTimeForegroundServiceUsed=" + lastTimeForegroundServiceUsed +
                ", totalTimeForegroundServiceUsed=" + totalTimeForegroundServiceUsed +
                ", launchCount=" + launchCount +
                '}';
    }

    @Override
    public int compareTo(UsageStatsData o) {
        return Long.compare(o.totalTimeVisible, totalTimeVisible);
    }
}
