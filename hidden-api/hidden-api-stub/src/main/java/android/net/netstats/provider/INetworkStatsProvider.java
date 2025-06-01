package android.net.netstats.provider;

public interface INetworkStatsProvider {
    void onRequestStatsUpdate(int token);
    void onSetAlert(long quotaBytes);
    void onSetWarningAndLimit(String iface, long warningBytes, long limitBytes);
}