package android.app;

import android.annotation.TargetApi;
import android.os.Binder;

public interface IStopUserCallback {
    @TargetApi(30)
    void userStopped(int userId);
    void userStopAborted(int userId);
    abstract class Stub extends Binder implements IStopUserCallback {

    }
}
