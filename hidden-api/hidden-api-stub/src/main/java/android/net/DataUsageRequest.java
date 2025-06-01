package android.net;

public final class DataUsageRequest  {

    public static final String PARCELABLE_KEY = "DataUsageRequest";
    public static final int REQUEST_ID_UNSET = 0;

    public final int requestId;

    public final NetworkTemplate template;

    /**
     * Threshold in bytes to be notified on.
     */
    public final long thresholdInBytes;

    public DataUsageRequest(int requestId, NetworkTemplate template, long thresholdInBytes) {
        this.requestId = requestId;
        this.template = template;
        this.thresholdInBytes = thresholdInBytes;
    }

}