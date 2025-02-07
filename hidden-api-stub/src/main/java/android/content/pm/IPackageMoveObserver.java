package android.content.pm;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public interface IPackageMoveObserver {
    void onCreated(int moveId, Bundle extras);

    void onStatusChanged(int moveId, int status, long estMillis);


    abstract class Stub extends Binder implements IPackageMoveObserver {

        public static IPackageMoveObserver asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}