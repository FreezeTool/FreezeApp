package android.content;

import android.os.Binder;
import android.os.IBinder;

public interface IOnPrimaryClipChangedListener {
    void dispatchPrimaryClipChanged();

    abstract class Stub extends Binder implements IOnPrimaryClipChangedListener {

    }
}
