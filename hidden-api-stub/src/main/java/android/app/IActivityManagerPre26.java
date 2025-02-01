package android.app;

import android.content.IContentProvider;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.john.hidden.api.Replace;

@Replace(IActivityManager.class)
public interface IActivityManagerPre26 extends IInterface {

    ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token)
            throws RemoteException;

    class ContentProviderHolder {

        public IContentProvider provider;
    }
}