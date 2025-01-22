package android.os;

import androidx.annotation.IntDef;

import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class UidBatteryConsumer extends BatteryConsumer {

    static final int CONSUMER_TYPE_UID = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_FOREGROUND,
            STATE_BACKGROUND
    })
    public @interface State {
    }

    /**
     * The state of an application when it is either running a foreground (top) activity.
     */
    public static final int STATE_FOREGROUND = 0;
    
    public static final int STATE_BACKGROUND = 1;

    static final int COLUMN_INDEX_UID = BatteryConsumer.COLUMN_COUNT;
    static final int COLUMN_INDEX_PACKAGE_WITH_HIGHEST_DRAIN = COLUMN_INDEX_UID + 1;
    static final int COLUMN_INDEX_TIME_IN_FOREGROUND = COLUMN_INDEX_UID + 2;
    static final int COLUMN_INDEX_TIME_IN_BACKGROUND = COLUMN_INDEX_UID + 3;
    static final int COLUMN_COUNT = BatteryConsumer.COLUMN_COUNT + 4;

    

    public int getUid() {
        throw new RuntimeException("STUB");
    }

    
    public String getPackageWithHighestDrain() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the amount of time in milliseconds this UID spent in the specified state.
     */
    public long getTimeInStateMs(@State int state) {
        throw new RuntimeException("STUB");
    }

    private void appendProcessStateData(StringBuilder sb, @ProcessState int processState,
            boolean skipEmptyComponents) {
        throw new RuntimeException("STUB");
    }

    static UidBatteryConsumer create(BatteryConsumerData data) {
        throw new RuntimeException("STUB");
    }
}