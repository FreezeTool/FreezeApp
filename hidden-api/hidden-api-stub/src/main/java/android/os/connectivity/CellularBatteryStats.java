package android.os.connectivity;


public final class CellularBatteryStats {


    public CellularBatteryStats(long loggingDurationMs, long kernelActiveTimeMs, long numPacketsTx,
            long numBytesTx, long numPacketsRx, long numBytesRx, long sleepTimeMs, long idleTimeMs,
            long rxTimeMs, Long energyConsumedMaMs, long[] timeInRatMs,
            long[] timeInRxSignalStrengthLevelMs, long[] txTimeMs,
            long monitoredRailChargeConsumedMaMs) {
        throw new RuntimeException("STUB");
    }


    /**
     * Returns the duration for which these cellular stats were collected.
     *
     * @return Duration of stats collection in milliseconds.
     */
    public long getLoggingDurationMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the kernel was active within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of kernel active time in milliseconds.
     */
    public long getKernelActiveTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of packets transmitted over cellular within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Number of packets transmitted.
     */
    public long getNumPacketsTx() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of packets received over cellular within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Number of packets received.
     */
    public long getNumBytesTx() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of bytes transmitted over cellular within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Number of bytes transmitted.
     */
    public long getNumPacketsRx() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the number of bytes received over cellular within
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
     * @return Duration of sleep time in milliseconds.
     */
    public long getSleepTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration for which the device was idle within
     * {@link #getLoggingDurationMillis()}.
     *
     * @return Duration of idle time in milliseconds.
     */
    public long getIdleTimeMillis() {
        throw new RuntimeException("STUB");
    }


    public long getRxTimeMillis() {
        throw new RuntimeException("STUB");
    }


    public long getEnergyConsumedMaMillis() {
        throw new RuntimeException("STUB");
    }


    public long getTimeInRatMicros(int networkType) {
        throw new RuntimeException("STUB");
    }


    public long getTimeInRxSignalStrengthLevelMicros(
            int signalStrengthBin) {
        throw new RuntimeException("STUB");
    }

    public long getTxTimeMillis(
           int level) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the energy consumed by cellular chip within {@link #getLoggingDurationMillis()}.
     *
     * @return Energy consumed in milli-ampere milli-seconds (mAmS).
     */
    public long getMonitoredRailChargeConsumedMaMillis() {
        throw new RuntimeException("STUB");
    }
}
