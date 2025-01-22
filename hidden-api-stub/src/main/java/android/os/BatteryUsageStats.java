package android.os;

import android.util.Range;

import androidx.annotation.RequiresApi;

import com.android.internal.os.BatteryStatsHistoryIterator;

import java.io.FileDescriptor;
import java.util.List;

public class BatteryUsageStats {
    
    
    public static final int AGGREGATE_BATTERY_CONSUMER_SCOPE_DEVICE = 0;

    public static final int AGGREGATE_BATTERY_CONSUMER_SCOPE_ALL_APPS = 1;

    public static final int AGGREGATE_BATTERY_CONSUMER_SCOPE_COUNT = 2;


    public long getStatsStartTimestamp() {
        throw new RuntimeException("STUB");
    }

    /**
     * Timestamp (as returned by System.currentTimeMillis()) of when the stats snapshot was taken,
     * in milliseconds.
     */
    public long getStatsEndTimestamp() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the duration of the stats session captured by this BatteryUsageStats.
     * In rare cases, statsDuration != statsEndTimestamp - statsStartTimestamp.  This may
     * happen when BatteryUsageStats represents an accumulation of data across multiple
     * non-contiguous sessions.
     */
    public long getStatsDuration() {
        throw new RuntimeException("STUB");
    }

    /**
     * Total amount of battery charge drained since BatteryStats reset (e.g. due to being fully
     * charged), in mAh
     */
    public double getConsumedPower() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns battery capacity in milli-amp-hours.
     */
    public double getBatteryCapacity() {
        throw new RuntimeException("STUB");
    }

    /**
     * Portion of battery charge drained since BatteryStats reset (e.g. due to being fully
     * charged), as percentage of the full charge in the range [0:100]. May exceed 100 if
     * the device repeatedly charged and discharged prior to the reset.
     */
    public int getDischargePercentage() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the discharged power since BatteryStats were last reset, in mAh as an estimated
     * range.
     */
    public Range<Double> getDischargedPowerRange() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the total amount of time the battery was discharging.
     */
    public long getDischargeDurationMs() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns an approximation for how much run time (in milliseconds) is remaining on
     * the battery.  Returns -1 if no time can be computed: either there is not
     * enough current data to make a decision, or the battery is currently
     * charging.
     */
    public long getBatteryTimeRemainingMs() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns an approximation for how much time (in milliseconds) remains until the battery
     * is fully charged.  Returns -1 if no time can be computed: either there is not
     * enough current data to make a decision, or the battery is currently discharging.
     */
    public long getChargeTimeRemainingMs() {
        throw new RuntimeException("STUB");
    }


    public List<UidBatteryConsumer> getUidBatteryConsumers() {
        throw new RuntimeException("STUB");
    }


    public List<UserBatteryConsumer> getUserBatteryConsumers() {
        throw new RuntimeException("STUB");
    }


    public String[] getCustomPowerComponentNames() {
        throw new RuntimeException("STUB");
    }

    public boolean isProcessStateDataIncluded() {
        throw new RuntimeException("STUB");
    }

    public BatteryStatsHistoryIterator iterateBatteryStatsHistory() {
        throw new RuntimeException("STUB");
    }


    /** Returns a proto (as used for atoms.proto) corresponding to this BatteryUsageStats. */
    public byte[] getStatsProto() {
        throw new RuntimeException("STUB");
    }

    /**
     * Writes contents in a binary protobuffer format, using
     * the android.os.BatteryUsageStatsAtomsProto proto.
     */
    public void dumpToProto(FileDescriptor fd) {
        throw new RuntimeException("STUB");
    }

//    @RequiresApi(34)
//    public AggregateBatteryConsumer getAggregateBatteryConsumer(int scope) {
//        throw new RuntimeException("STUB");
//    }
//
//    @RequiresApi(31) // android 31 - S
//    public BatteryConsumer getAggregateBatteryConsumer(int scope) {
//        throw new RuntimeException("STUB");
//    }
}