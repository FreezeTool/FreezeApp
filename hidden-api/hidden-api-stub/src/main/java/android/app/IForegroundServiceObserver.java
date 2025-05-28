package android.app;

import android.os.Binder;
import android.os.IBinder;

public interface IForegroundServiceObserver {

    void onForegroundStateChanged(IBinder serviceToken, String packageName, int userId, boolean isForeground);

    abstract class Stub extends Binder implements IForegroundServiceObserver {

    }
}
