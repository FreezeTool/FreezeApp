package android.content.pm;

import android.os.Binder;
import android.os.IBinder;

public interface IPackageDeleteObserver {
    void packageDeleted(String packageName, int returnCode);

    abstract class Stub extends Binder implements IPackageDeleteObserver {

        public static IPackageDeleteObserver asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}