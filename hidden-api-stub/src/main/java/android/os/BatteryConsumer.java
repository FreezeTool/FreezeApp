package android.os;
import androidx.annotation.IntDef;

import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class BatteryConsumer {

    private static final String TAG = "BatteryConsumer";

    /**
     * Power usage component, describing the particular part of the system
     * responsible for power drain.
     *
     * @hide
     */
    @IntDef(value = {
            POWER_COMPONENT_ANY,
            POWER_COMPONENT_SCREEN,
            POWER_COMPONENT_CPU,
            POWER_COMPONENT_BLUETOOTH,
            POWER_COMPONENT_CAMERA,
            POWER_COMPONENT_AUDIO,
            POWER_COMPONENT_VIDEO,
            POWER_COMPONENT_FLASHLIGHT,
            POWER_COMPONENT_MOBILE_RADIO,
            POWER_COMPONENT_SYSTEM_SERVICES,
            POWER_COMPONENT_SENSORS,
            POWER_COMPONENT_GNSS,
            POWER_COMPONENT_WIFI,
            POWER_COMPONENT_WAKELOCK,
            POWER_COMPONENT_MEMORY,
            POWER_COMPONENT_PHONE,
            POWER_COMPONENT_IDLE,
            POWER_COMPONENT_REATTRIBUTED_TO_OTHER_CONSUMERS,
    })
    @Retention(RetentionPolicy.SOURCE)
    public static @interface PowerComponent {
    }

    public static final int POWER_COMPONENT_ANY = -1;
    public static final int POWER_COMPONENT_SCREEN = 0; // 0
    public static final int POWER_COMPONENT_CPU = 1; // 1
    public static final int POWER_COMPONENT_BLUETOOTH = 2; // 2
    public static final int POWER_COMPONENT_CAMERA = 3; // 3
    public static final int POWER_COMPONENT_AUDIO = 4; // 4
    public static final int POWER_COMPONENT_VIDEO = 5; // 5
    public static final int POWER_COMPONENT_FLASHLIGHT =
            6; // 6
    public static final int POWER_COMPONENT_SYSTEM_SERVICES =
            7; // 7
    public static final int POWER_COMPONENT_MOBILE_RADIO =
            8; // 8
    public static final int POWER_COMPONENT_SENSORS = 9; // 9
    public static final int POWER_COMPONENT_GNSS = 10; // 10
    public static final int POWER_COMPONENT_WIFI = 11; // 11
    public static final int POWER_COMPONENT_WAKELOCK = 12; // 12
    public static final int POWER_COMPONENT_MEMORY = 13; // 13
    public static final int POWER_COMPONENT_PHONE = 14; // 14
    public static final int POWER_COMPONENT_AMBIENT_DISPLAY =
            15; // 15
    public static final int POWER_COMPONENT_IDLE = 16; // 16
    // Power that is re-attributed to other battery consumers. For example, for System Server
    // this represents the power attributed to apps requesting system services.
    // The value should be negative or zero.
    public static final int POWER_COMPONENT_REATTRIBUTED_TO_OTHER_CONSUMERS =
            17; // 17

    public static final int POWER_COMPONENT_COUNT = 18;

    public static final int FIRST_CUSTOM_POWER_COMPONENT_ID = 1000;
    public static final int LAST_CUSTOM_POWER_COMPONENT_ID = 9999;

    private static final String[] sPowerComponentNames = new String[POWER_COMPONENT_COUNT];

    static {
        // Assign individually to avoid future mismatch
        sPowerComponentNames[POWER_COMPONENT_SCREEN] = "screen";
        sPowerComponentNames[POWER_COMPONENT_CPU] = "cpu";
        sPowerComponentNames[POWER_COMPONENT_BLUETOOTH] = "bluetooth";
        sPowerComponentNames[POWER_COMPONENT_CAMERA] = "camera";
        sPowerComponentNames[POWER_COMPONENT_AUDIO] = "audio";
        sPowerComponentNames[POWER_COMPONENT_VIDEO] = "video";
        sPowerComponentNames[POWER_COMPONENT_FLASHLIGHT] = "flashlight";
        sPowerComponentNames[POWER_COMPONENT_SYSTEM_SERVICES] = "system_services";
        sPowerComponentNames[POWER_COMPONENT_MOBILE_RADIO] = "mobile_radio";
        sPowerComponentNames[POWER_COMPONENT_SENSORS] = "sensors";
        sPowerComponentNames[POWER_COMPONENT_GNSS] = "gnss";
        sPowerComponentNames[POWER_COMPONENT_WIFI] = "wifi";
        sPowerComponentNames[POWER_COMPONENT_WAKELOCK] = "wakelock";
        sPowerComponentNames[POWER_COMPONENT_MEMORY] = "memory";
        sPowerComponentNames[POWER_COMPONENT_PHONE] = "phone";
        sPowerComponentNames[POWER_COMPONENT_AMBIENT_DISPLAY] = "ambient_display";
        sPowerComponentNames[POWER_COMPONENT_IDLE] = "idle";
        sPowerComponentNames[POWER_COMPONENT_REATTRIBUTED_TO_OTHER_CONSUMERS] = "reattributed";
    }


    @IntDef( value = {
            POWER_MODEL_UNDEFINED,
            POWER_MODEL_POWER_PROFILE,
            POWER_MODEL_MEASURED_ENERGY,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PowerModel {
    }

    /**
     * Unspecified power model.
     */
    public static final int POWER_MODEL_UNDEFINED = 0;

    /**
     * Power model that is based on average consumption rates that hardware components
     * consume in various states.
     */
    public static final int POWER_MODEL_POWER_PROFILE = 1;

    /**
     * Power model that is based on energy consumption measured by on-device power monitors.
     */
    public static final int POWER_MODEL_MEASURED_ENERGY = 2;


    @IntDef( value = {
            PROCESS_STATE_ANY,
            PROCESS_STATE_UNSPECIFIED,
            PROCESS_STATE_FOREGROUND,
            PROCESS_STATE_BACKGROUND,
            PROCESS_STATE_FOREGROUND_SERVICE,
            PROCESS_STATE_CACHED,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProcessState {
    }

    public static final int PROCESS_STATE_UNSPECIFIED = 0;
    public static final int PROCESS_STATE_ANY = PROCESS_STATE_UNSPECIFIED;
    public static final int PROCESS_STATE_FOREGROUND = 1;
    public static final int PROCESS_STATE_BACKGROUND = 2;
    public static final int PROCESS_STATE_FOREGROUND_SERVICE = 3;
    public static final int PROCESS_STATE_CACHED = 4;

    public static final int PROCESS_STATE_COUNT = 5;

    private static final String[] sProcessStateNames = new String[PROCESS_STATE_COUNT];

    static {
        // Assign individually to avoid future mismatch
        sProcessStateNames[PROCESS_STATE_UNSPECIFIED] = "unspecified";
        sProcessStateNames[PROCESS_STATE_FOREGROUND] = "fg";
        sProcessStateNames[PROCESS_STATE_BACKGROUND] = "bg";
        sProcessStateNames[PROCESS_STATE_FOREGROUND_SERVICE] = "fgs";
        sProcessStateNames[PROCESS_STATE_CACHED] = "cached";
    }

    private static final int[] SUPPORTED_POWER_COMPONENTS_PER_PROCESS_STATE = {
            POWER_COMPONENT_CPU,
            POWER_COMPONENT_MOBILE_RADIO,
            POWER_COMPONENT_WIFI,
            POWER_COMPONENT_BLUETOOTH,
    };

    static final int COLUMN_INDEX_BATTERY_CONSUMER_TYPE = 0;
    static final int COLUMN_COUNT = 1;

    /**
     * Identifies power attribution dimensions that a caller is interested in.
     */
    public static final class Dimensions {
        public final @PowerComponent int powerComponent;
        public final @ProcessState int processState;

        public Dimensions(int powerComponent, int processState) {
            this.powerComponent = powerComponent;
            this.processState = processState;
        }

        @Override
        public String toString() {
            boolean dimensionSpecified = false;
            StringBuilder sb = new StringBuilder();
            if (powerComponent != POWER_COMPONENT_ANY) {
                sb.append("powerComponent=").append(sPowerComponentNames[powerComponent]);
                dimensionSpecified = true;
            }
            if (processState != PROCESS_STATE_UNSPECIFIED) {
                if (dimensionSpecified) {
                    sb.append(", ");
                }
                sb.append("processState=").append(sProcessStateNames[processState]);
                dimensionSpecified = true;
            }
            if (!dimensionSpecified) {
                sb.append("any components and process states");
            }
            return sb.toString();
        }
    }

    public static final Dimensions UNSPECIFIED_DIMENSIONS =
            new Dimensions(POWER_COMPONENT_ANY, PROCESS_STATE_ANY);

    /**
     * Identifies power attribution dimensions that are captured by an data element of
     * a BatteryConsumer. These Keys are used to access those values and to set them using
     * Builders.  See for example {@link #getConsumedPower(Key)}.
     *
     * Keys cannot be allocated by the client - they can only be obtained by calling
     * {@link #getKeys} or {@link #getKey}.  All BatteryConsumers that are part of the
     * same BatteryUsageStats share the same set of keys, therefore it is safe to obtain
     * the keys from one BatteryConsumer and apply them to other BatteryConsumers
     * in the same BatteryUsageStats.
     */
    public static final class Key {
        public final @PowerComponent int powerComponent;
        public final @ProcessState int processState;

        final int mPowerModelColumnIndex;
        final int mPowerColumnIndex;
        final int mDurationColumnIndex;
        private String mShortString;

        private Key(int powerComponent, int processState, int powerModelColumnIndex,
                int powerColumnIndex, int durationColumnIndex) {
            this.powerComponent = powerComponent;
            this.processState = processState;

            mPowerModelColumnIndex = powerModelColumnIndex;
            mPowerColumnIndex = powerColumnIndex;
            mDurationColumnIndex = durationColumnIndex;
        }

        @SuppressWarnings("EqualsUnsafeCast")
        @Override
        public boolean equals(Object o) {
            // Skipping null and class check for performance
            final Key key = (Key) o;
            return powerComponent == key.powerComponent
                && processState == key.processState;
        }

        @Override
        public int hashCode() {
            int result = powerComponent;
            result = 31 * result + processState;
            return result;
        }

        /**
         * Returns a string suitable for use in dumpsys.
         */
        public String toShortString() {
            if (mShortString == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(powerComponentIdToString(powerComponent));
                if (processState != PROCESS_STATE_UNSPECIFIED) {
                    sb.append(':');
                    sb.append(processStateToString(processState));
                }
                mShortString = sb.toString();
            }
            return mShortString;
        }
    }

    /**
     * Total power consumed by this consumer, in mAh.
     */
    public double getConsumedPower() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns power consumed aggregated over the specified dimensions, in mAh.
     */
    public double getConsumedPower(Dimensions dimensions) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns keys for various power values attributed to the specified component
     * held by this BatteryUsageStats object.
     */
    public Key[] getKeys(@PowerComponent int componentId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the key for the power attributed to the specified component,
     * for all values of other dimensions such as process state.
     */
    public Key getKey(@PowerComponent int componentId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the key for the power attributed to the specified component and process state.
     */
    public Key getKey(@PowerComponent int componentId, @ProcessState int processState) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the amount of drain attributed to the specified drain type, e.g. CPU, WiFi etc.
     *
     * @param componentId The ID of the power component, e.g.
     *                    {@link BatteryConsumer#POWER_COMPONENT_CPU}.
     * @return Amount of consumed power in mAh.
     */
    public double getConsumedPower(@PowerComponent int componentId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the amount of drain attributed to the specified drain type, e.g. CPU, WiFi etc.
     *
     * @param key The key of the power component, obtained by calling {@link #getKey} or
     *            {@link #getKeys} method.
     * @return Amount of consumed power in mAh.
     */
    public double getConsumedPower( Key key) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the ID of the model that was used for power estimation.
     *
     * @param componentId The ID of the power component, e.g.
     *                    {@link BatteryConsumer#POWER_COMPONENT_CPU}.
     */
    public @PowerModel int getPowerModel(@BatteryConsumer.PowerComponent int componentId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the ID of the model that was used for power estimation.
     *
     * @param key The key of the power component, obtained by calling {@link #getKey} or
     *            {@link #getKeys} method.
     */
    public @PowerModel int getPowerModel( BatteryConsumer.Key key) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the amount of drain attributed to the specified custom drain type.
     *
     * @param componentId The ID of the custom power component.
     * @return Amount of consumed power in mAh.
     */
    public double getConsumedPowerForCustomComponent(int componentId) {
        throw new RuntimeException("STUB");
    }

    public int getCustomPowerComponentCount() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the name of the specified power component.
     *
     * @param componentId The ID of the custom power component.
     */
    public String getCustomPowerComponentName(int componentId) {
        throw new RuntimeException("STUB");
    }

    public long getUsageDurationMillis(@PowerComponent int componentId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the amount of time since BatteryStats reset used by the specified component, e.g.
     * CPU, WiFi etc.
     *
     *
     * @param key The key of the power component, obtained by calling {@link #getKey} or
     *            {@link #getKeys} method.
     * @return Amount of time in milliseconds.
     */
    public long getUsageDurationMillis( Key key) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the amount of usage time attributed to the specified custom component
     * since BatteryStats reset.
     *
     * @param componentId The ID of the custom power component.
     * @return Amount of time in milliseconds.
     */
    public long getUsageDurationForCustomComponentMillis(int componentId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the name of the specified component.  Intended for logging and debugging.
     */
    public static String powerComponentIdToString(@BatteryConsumer.PowerComponent int componentId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the name of the specified power model.  Intended for logging and debugging.
     */
    public static String powerModelToString(@BatteryConsumer.PowerModel int powerModel) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the name of the specified process state.  Intended for logging and debugging.
     */
    public static String processStateToString(@BatteryConsumer.ProcessState int processState) {
        throw new RuntimeException("STUB");
    }

    /**
     * Prints the stats in a human-readable format.
     */
    public void dump(PrintWriter pw) {
        throw new RuntimeException("STUB");
    }

    /**
     * Prints the stats in a human-readable format.
     *
     * @param skipEmptyComponents if true, omit any power components with a zero amount.
     */
    public void dump(PrintWriter pw, boolean skipEmptyComponents) {
        throw new RuntimeException("STUB");
    }


    /** Converts charge from milliamp hours (mAh) to decicoulombs (dC). */
    static long convertMahToDeciCoulombs(double powerMah) {
        return (long) (powerMah * (10 * 3600 / 1000) + 0.5);
    }

    static class BatteryConsumerData {

        public Key[] getKeys(int componentId) {
            throw new RuntimeException("STUB");
        }

        Key getKeyOrThrow(int componentId, int processState) {
            throw new RuntimeException("STUB");
        }

        Key getKey(int componentId, int processState) {
            throw new RuntimeException("STUB");
        }
    }

    static class BatteryConsumerDataLayout {
        public String[] customPowerComponentNames;
        public int customPowerComponentCount;
        public  boolean powerModelsIncluded;
        public  boolean processStateDataIncluded;
        public  Key[][] keys;
        public  int totalConsumedPowerColumnIndex;
        public  int firstCustomConsumedPowerColumn;
        public  int firstCustomUsageDurationColumn;
        public  int columnCount;
        public  Key[][] processStateKeys;
    }

    static BatteryConsumerDataLayout createBatteryConsumerDataLayout(
            String[] customPowerComponentNames, boolean includePowerModels,
            boolean includeProcessStateData) {
        throw new RuntimeException("STUB");
    }

    protected abstract static class BaseBuilder<T extends BaseBuilder<?>> {



        public Key[] getKeys(@PowerComponent int componentId) {
            throw new RuntimeException("STUB");
        }


        public Key getKey(@PowerComponent int componentId, @ProcessState int processState) {
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the amount of drain attributed to the specified drain type, e.g. CPU, WiFi etc.
         *
         * @param componentId    The ID of the power component, e.g.
         *                       {@link BatteryConsumer#POWER_COMPONENT_CPU}.
         * @param componentPower Amount of consumed power in mAh.
         */

        public T setConsumedPower(@PowerComponent int componentId, double componentPower) {
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the amount of drain attributed to the specified drain type, e.g. CPU, WiFi etc.
         *
         * @param componentId    The ID of the power component, e.g.
         *                       {@link BatteryConsumer#POWER_COMPONENT_CPU}.
         * @param componentPower Amount of consumed power in mAh.
         */
        @SuppressWarnings("unchecked")

        public T setConsumedPower(@PowerComponent int componentId, double componentPower,
                @PowerModel int powerModel) {
            throw new RuntimeException("STUB");
        }


        public T setConsumedPower(Key key, double componentPower, @PowerModel int powerModel) {
            throw new RuntimeException("STUB");
        }



        public T setConsumedPowerForCustomComponent(int componentId, double componentPower) {
            throw new RuntimeException("STUB");
        }


        public T setUsageDurationMillis(@UidBatteryConsumer.PowerComponent int componentId,
                long componentUsageTimeMillis) {
            throw new RuntimeException("STUB");
        }


        @SuppressWarnings("unchecked")

        public T setUsageDurationMillis(Key key, long componentUsageTimeMillis) {
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the amount of time used by the specified custom component.
         *
         * @param componentId              The ID of the custom power component.
         * @param componentUsageTimeMillis Amount of time in microseconds.
         */
        @SuppressWarnings("unchecked")

        public T setUsageDurationForCustomComponentMillis(int componentId,
                long componentUsageTimeMillis) {
            throw new RuntimeException("STUB");
        }

        /**
         * Returns the total power accumulated by this builder so far. It may change
         * by the time the {@code build()} method is called.
         */
        public double getTotalPower() {
            throw new RuntimeException("STUB");
        }
    }
}