package android.os.connectivity;

public final class WifiBatteryStats  {
    
    public WifiBatteryStats(long loggingDurationMillis, long kernelActiveTimeMillis,
            long numPacketsTx, long numBytesTx, long numPacketsRx, long numBytesRx,
            long sleepTimeMillis, long scanTimeMillis, long idleTimeMillis, long rxTimeMillis,
            long txTimeMillis, long energyConsumedMaMillis, long appScanRequestCount,
             long[] timeInStateMillis,  long [] timeInRxSignalStrengthLevelMillis,
             long[] timeInSupplicantStateMillis, long monitoredRailChargeConsumedMaMillis) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which these wifi stats were collected.
     *
     * @return Duration of stats collection in millis.
     */
    public long getLoggingDurationMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the kernel was active within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of kernel active time in millis.
     */
    public long getKernelActiveTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of packets transmitted over wifi within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Number of packets transmitted.
     */
    public long getNumPacketsTx() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of bytes transmitted over wifi within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Number of bytes transmitted.
     */
    public long getNumBytesTx() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of packets received over wifi within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Number of packets received.
     */
    public long getNumPacketsRx() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of bytes received over wifi within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Number of bytes received.
     */
    public long getNumBytesRx() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the device was sleeping within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of sleep time in millis.
     */
    public long getSleepTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the device was wifi scanning within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of wifi scanning time in millis.
     */
    public long getScanTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the device was idle within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of idle time in millis.
     */
    public long getIdleTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the device was receiving over wifi within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of wifi reception time in millis.
     */
    public long getRxTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the device was transmitting over wifi within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of wifi transmission time in millis.
     */
    public long getTxTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns an estimation of energy consumed in millis by wifi chip within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Energy consumed in millis.
     */
    public long getEnergyConsumedMaMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of app initiated wifi scans within {@link #getLoggingDurationMillis()}.
     *
     * @return Number of app scans.
     */
    public long getAppScanRequestCount() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the energy consumed by wifi chip within {@link #getLoggingDurationMillis()}.
     *
     * @return Energy consumed in millis.
     */
    public long getMonitoredRailChargeConsumedMaMillis() {
        throw new RuntimeException("STUB");
    }
}