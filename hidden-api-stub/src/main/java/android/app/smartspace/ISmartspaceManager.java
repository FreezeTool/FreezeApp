package android.app.smartspace;

import android.app.IActivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public interface ISmartspaceManager {

    void createSmartspaceSession(SmartspaceConfig config, SmartspaceSessionId sessionId,
             IBinder token) throws RemoteException;

    void notifySmartspaceEvent( SmartspaceSessionId sessionId,  SmartspaceTargetEvent event) throws RemoteException;

    void requestSmartspaceUpdate(SmartspaceSessionId sessionId);

    void registerSmartspaceUpdates(SmartspaceSessionId sessionId,
            ISmartspaceCallback callback);

    void unregisterSmartspaceUpdates(SmartspaceSessionId sessionId,
            ISmartspaceCallback callback);

    void destroySmartspaceSession(SmartspaceSessionId sessionId);

    abstract class Stub extends Binder implements ISmartspaceManager {

        public static ISmartspaceManager asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}