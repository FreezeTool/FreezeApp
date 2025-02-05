package android.app.usage;


import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.john.hidden.api.Replace;

@Replace(UsageStats.class)
public class UsageStatsHidden {
    public int mLaunchCount;
    @RequiresApi(Build.VERSION_CODES.P)
    public int mAppLaunchCount;
}
