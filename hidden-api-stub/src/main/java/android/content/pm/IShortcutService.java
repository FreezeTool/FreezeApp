package android.content.pm;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import java.util.List;

@RequiresApi(Build.VERSION_CODES.O)
public interface IShortcutService {

    void removeDynamicShortcuts(String packageName, List<String> shortcutIds, int userId)
            throws RemoteException;

    abstract class Stub extends Binder implements IShortcutService {

        public static IShortcutService asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }
}
