package android.net.netstats;

import android.net.DataUsageRequest;
import android.net.INetworkStatsService;
import android.os.Binder;
import android.os.IBinder;

public interface IUsageCallback {
    void onThresholdReached(DataUsageRequest request);

    void onCallbackReleased(DataUsageRequest request);

    abstract class Stub extends Binder implements IUsageCallback {
        public static IUsageCallback asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
