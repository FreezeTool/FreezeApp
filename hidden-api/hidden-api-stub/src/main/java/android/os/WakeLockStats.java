package android.os;

import java.util.List;

public final class WakeLockStats {


    public static class WakeLock {
        public int uid;
        public String name;
        public int timesAcquired;
        public long totalTimeHeldMs;


        public long timeHeldMs;
    }

    public List<WakeLock> getWakeLocks() {
        throw new RuntimeException("STUB");
    }

}