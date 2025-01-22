package android.os;

public final class BatteryUsageStatsQuery  {

    public static final BatteryUsageStatsQuery DEFAULT =
            new BatteryUsageStatsQuery.Builder().build();

    public static final int FLAG_BATTERY_USAGE_STATS_POWER_PROFILE_MODEL = 0x0001;

    public static final int FLAG_BATTERY_USAGE_STATS_INCLUDE_HISTORY = 0x0002;

    public static final int FLAG_BATTERY_USAGE_STATS_INCLUDE_POWER_MODELS = 0x0004;

    public static final int FLAG_BATTERY_USAGE_STATS_INCLUDE_PROCESS_STATE_DATA = 0x0008;

    public static final int FLAG_BATTERY_USAGE_STATS_INCLUDE_VIRTUAL_UIDS = 0x0010;


    private BatteryUsageStatsQuery(Builder builder) {

    }

    public int getFlags() {
        throw new RuntimeException("STUB");
    }

    public int[] getUserIds() {
        throw new RuntimeException("STUB");
    }


    public boolean shouldForceUsePowerProfileModel() {
        throw new RuntimeException("STUB");
    }

    public boolean isProcessStateDataNeeded() {
        throw new RuntimeException("STUB");
    }


    public int[] getPowerComponents() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the client's tolerance for stale battery stats. The data is allowed to be up to
     * this many milliseconds out-of-date.
     */
    public long getMaxStatsAge() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the exclusive lower bound of the stored snapshot timestamps that should be included
     * in the aggregation.  Ignored if {@link #getToTimestamp()} is zero.
     */
    public long getFromTimestamp() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the inclusive upper bound of the stored snapshot timestamps that should
     * be included in the aggregation.  The default is to include only the current stats
     * accumulated since the latest battery reset.
     */
    public long getToTimestamp() {
        throw new RuntimeException("STUB");
    }

    public static final class Builder {

        /**
         * Builds a read-only BatteryUsageStatsQuery object.
         */
        public BatteryUsageStatsQuery build() {
            throw new RuntimeException("STUB");
        }

        public Builder addUser( UserHandle userHandle) {
            throw new RuntimeException("STUB");
        }

        /**
         * Requests that battery history be included in the BatteryUsageStats.
         */
        public Builder includeBatteryHistory() {
            throw new RuntimeException("STUB");
        }

        public Builder includeProcessStateData() {
            throw new RuntimeException("STUB");
        }

        public Builder powerProfileModeledOnly() {
            throw new RuntimeException("STUB");
        }

        /**
         * Requests to return identifiers of models that were used for estimation
         * of power consumption.
         *
         * Should only be used for testing and debugging.
         */
        public Builder includePowerModels() {
            throw new RuntimeException("STUB");
        }

        /**
         * Requests to return only statistics for the specified power components.  The default
         * is all power components.
         */
        public Builder includePowerComponents(
                @BatteryConsumer.PowerComponent int[] powerComponents) {
            throw new RuntimeException("STUB");
        }


        public Builder includeVirtualUids() {
            throw new RuntimeException("STUB");
        }


        public Builder aggregateSnapshots(long fromTimestamp, long toTimestamp) {
            throw new RuntimeException("STUB");
        }

        /**
         * Set the client's tolerance for stale battery stats. The data may be up to
         * this many milliseconds out-of-date.
         */
        public Builder setMaxStatsAgeMs(long maxStatsAgeMs) {
            throw new RuntimeException("STUB");
        }
    }
}