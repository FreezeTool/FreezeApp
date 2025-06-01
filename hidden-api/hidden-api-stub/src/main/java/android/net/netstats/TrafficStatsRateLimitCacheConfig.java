package android.net.netstats;

public class TrafficStatsRateLimitCacheConfig {
    /**
     * Whether the cache is enabled for V+ device or target Sdk V+ apps.
     */
    public boolean isCacheEnabled;

    /**
     * The duration for which cache entries are valid, in milliseconds.
     */
    public int expiryDurationMs;

    /**
     * The maximum number of entries to store in the cache.
     */
    public int maxEntries;
}
