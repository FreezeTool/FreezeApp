package android.app;

import android.os.Binder;
import android.os.IRemoteCallback;

public interface IUserSwitchObserver {
    void onUserSwitching(int newUserId, IRemoteCallback reply);
    void onUserSwitchComplete(int newUserId);
    void onForegroundProfileSwitch(int newProfileId);
    void onLockedBootComplete(int newUserId);

    abstract class Stub extends Binder implements IUserSwitchObserver {

    }
}
