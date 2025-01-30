package android.os;

import android.app.IUserSwitchObserver;

public interface IRemoteCallback {
    void sendResult(Bundle data);
    abstract class Stub extends Binder implements IRemoteCallback {

    }
}
