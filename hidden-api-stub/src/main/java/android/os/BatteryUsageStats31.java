package android.os;

import androidx.annotation.RequiresApi;

import com.john.hidden.api.Replace;

@Replace(BatteryUsageStats.class)
public class BatteryUsageStats31 {
    @RequiresApi(31) // android 31 - S
    public BatteryConsumer getAggregateBatteryConsumer(int scope) {
        throw new RuntimeException("STUB");
    }
}
