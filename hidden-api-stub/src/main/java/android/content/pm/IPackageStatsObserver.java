package android.content.pm;

import android.os.Binder;
import android.os.IBinder;

public interface IPackageStatsObserver {

    void onGetStatsCompleted(PackageStats pStats, boolean succeeded);

    abstract class Stub extends Binder implements IPackageStatsObserver {

        public static IPackageStatsObserver asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}