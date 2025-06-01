package android.net;

public interface INetworkStatsSession {

    /** Return device aggregated network layer usage summary for traffic that matches template. */
    NetworkStats getDeviceSummaryForNetwork(NetworkTemplate template, long start, long end);

    /** Return network layer usage summary for traffic that matches template. */
    
    NetworkStats getSummaryForNetwork(NetworkTemplate template, long start, long end);
    /** Return historical network layer stats for traffic that matches template. */
    
    NetworkStatsHistory getHistoryForNetwork(NetworkTemplate template, int fields);
    /**
     * Return historical network layer stats for traffic that matches template, start and end
     * timestamp.
     */
    NetworkStatsHistory getHistoryIntervalForNetwork(NetworkTemplate template, int fields, long start, long end);

    /**
     * Return network layer usage summary per UID for traffic that matches template.
     *
     * <p>The resulting {@code NetworkStats#getElapsedRealtime()} contains time delta between
     * {@code start} and {@code end}.
     *
     * @param template - a predicate to filter netstats.
     * @param start - start of the range, timestamp milliseconds since the epoch.
     * @param end - end of the range, timestamp milliseconds since the epoch.
     * @param includeTags - includes data usage tags if true.
     */
    
    NetworkStats getSummaryForAllUid(NetworkTemplate template, long start, long end, boolean includeTags);

    /** Return network layer usage summary per UID for tagged traffic that matches template. */
    NetworkStats getTaggedSummaryForAllUid(NetworkTemplate template, long start, long end);

    /** Return historical network layer stats for specific UID traffic that matches template. */
    
    NetworkStatsHistory getHistoryForUid(NetworkTemplate template, int uid, int set, int tag, int fields);
    /** Return historical network layer stats for specific UID traffic that matches template. */
    NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate template, int uid, int set, int tag, int fields, long start, long end);


    int[] getRelevantUids();

    
    void close();

}