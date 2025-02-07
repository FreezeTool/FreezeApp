package android.content.pm;

import android.os.Binder;
import android.os.IBinder;

public interface IPackageDataObserver {
    void onRemoveCompleted(String packageName, boolean succeeded);

    abstract class Stub extends Binder implements IPackageDataObserver {

        public static IPackageDataObserver asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
