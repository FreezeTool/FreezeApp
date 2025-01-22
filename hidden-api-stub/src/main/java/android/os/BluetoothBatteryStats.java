package android.os;

import java.util.List;

public class BluetoothBatteryStats {

    /** @hide */
    public static class UidStats {
        public  int uid;
        public  long scanTimeMs;
        public  long unoptimizedScanTimeMs;
        public  int scanResultCount;
        public  long rxTimeMs;
        public  long txTimeMs;
    }

    public List<UidStats> getUidStats() {
        throw new RuntimeException("STUB");
    }
}