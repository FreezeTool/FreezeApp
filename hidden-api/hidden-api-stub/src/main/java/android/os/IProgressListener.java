package android.os;

import android.app.IUserSwitchObserver;

public interface IProgressListener {
    void onStarted(int id, Bundle extras);

    void onProgress(int id, int progress, Bundle extras);

    void onFinished(int id, Bundle extras);

    abstract class Stub extends Binder implements IProgressListener {

    }
}
