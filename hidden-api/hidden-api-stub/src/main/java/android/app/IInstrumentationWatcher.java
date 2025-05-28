package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;

public interface IInstrumentationWatcher {
    void instrumentationStatus(ComponentName name, int resultCode,
                                Bundle results);
    void instrumentationFinished(ComponentName name, int resultCode,
                                 Bundle results);

    abstract class Stub extends Binder implements IInstrumentationWatcher {

    }
}
