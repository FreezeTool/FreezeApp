package android.app.usage;

import com.john.hidden.api.Replace;

@Replace(UsageStatsManager.class)
public class UsageStatsManagerHidden {
    public static int STANDBY_BUCKET_EXEMPTED;
    public static int STANDBY_BUCKET_NEVER;
}
