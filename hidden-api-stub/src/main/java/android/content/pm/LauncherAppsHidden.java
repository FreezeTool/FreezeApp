package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;

import com.john.hidden.api.Replace;

@Replace(LauncherApps.class)
public class LauncherAppsHidden {
    public static final class AppUsageLimit {
        private final long mTotalUsageLimit;
        private final long mUsageRemaining;

        public AppUsageLimit(long totalUsageLimit, long usageRemaining) {
            this.mTotalUsageLimit = totalUsageLimit;
            this.mUsageRemaining = usageRemaining;
        }

        /**
         * Returns the total usage limit in milliseconds set for an app or a group of apps.
         *
         * @return the total usage limit in milliseconds
         */
        public long getTotalUsageLimit() {
            return mTotalUsageLimit;
        }

        /**
         * Returns the usage remaining in milliseconds for an app or the group of apps
         * this limit refers to.
         *
         * @return the usage remaining in milliseconds
         */
        public long getUsageRemaining() {
            return mUsageRemaining;
        }


    }
}
