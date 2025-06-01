package android.net;


import android.annotation.TargetApi;
import android.net.netstats.IUsageCallback;
import android.net.netstats.TrafficStatsRateLimitCacheConfig;
import android.net.netstats.provider.INetworkStatsProvider;
import android.net.netstats.provider.INetworkStatsProviderCallback;
import android.net.netstats.StatsResult;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Messenger;

public interface INetworkStatsService {
    /**
     * Start a statistics query session.
     */

    INetworkStatsSession openSession();

    INetworkStatsSession openSessionForUsageStats(int flags, String callingPackage);


    NetworkStats getDataLayerSnapshotForUid(int uid);

    NetworkStats getUidStatsForTransport(int transport);


    String[] getMobileIfaces();

    /**
     * Increment data layer count of operations performed for UID and tag.
     */
    void incrementOperationCount(int uid, int tag, int operationCount);

    /**
     * Notify {@code NetworkStatsService} about network status changed.
     */
    void notifyNetworkStatus(
            Network[] defaultNetworks,
            NetworkStateSnapshot[] snapshots,
            String activeIface,
            UnderlyingNetworkInfo[] underlyingNetworkInfos);

    /**
     * Force update of statistics.
     */

    void forceUpdate();

    //android.net.connectivity.android.net.netstats.IUsageCallback
    @TargetApi(36)
    DataUsageRequest registerUsageCallback(String callingPackage, DataUsageRequest request, android.net.connectivity.android.net.netstats.IUsageCallback callback);

    /**
     * Registers a callback on data usage.
     */
    @TargetApi(Build.VERSION_CODES.TIRAMISU)
    DataUsageRequest registerUsageCallback(String callingPackage, DataUsageRequest request, IUsageCallback callback);

    @TargetApi(Build.VERSION_CODES.N)
    DataUsageRequest registerUsageCallback(String callingPackage, DataUsageRequest request, Messenger messenger, IBinder binder);


    /**
     * Unregisters a callback on data usage.
     */
    void unregisterUsageRequest(DataUsageRequest request);

    /**
     * Get the uid stats information since boot
     */
    StatsResult getUidStats(int uid);

    /**
     * Get the iface stats information since boot
     */
    StatsResult getIfaceStats(String iface);

    /**
     * Get the total network stats information since boot
     */
    StatsResult getTotalStats();

    /**
     * Registers a network stats provider
     */
    INetworkStatsProviderCallback registerNetworkStatsProvider(String tag,
                                                               INetworkStatsProvider provider);

    /**
     * Mark given UID as being foreground for stats purposes.
     */
    void noteUidForeground(int uid, boolean uidForeground);

    /**
     * Advise persistence threshold; may be overridden internally.
     */
    void advisePersistThreshold(long thresholdBytes);

    /**
     * Set the warning and limit to all registered custom network stats providers.
     * Note that invocation of any interface will be sent to all providers.
     */
    void setStatsProviderWarningAndLimitAsync(String iface, long warning, long limit);

    /**
     * Clear TrafficStats rate-limit caches.
     */
    void clearTrafficStatsRateLimitCaches();

    /**
     * Get rate-limit cache config.
     */
    TrafficStatsRateLimitCacheConfig getRateLimitCacheConfig();


    abstract class Stub extends Binder implements INetworkStatsService {
        public static INetworkStatsService asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
