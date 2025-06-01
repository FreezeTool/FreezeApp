package android.net.netstats.provider;

import android.net.NetworkStats;

public interface INetworkStatsProviderCallback {
    void notifyStatsUpdated(int token, NetworkStats ifaceStats, NetworkStats uidStats);
    void notifyAlertReached();
    void notifyWarningReached();
    void notifyLimitReached();
    void unregister();
}