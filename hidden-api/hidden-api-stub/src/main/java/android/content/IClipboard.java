package android.content;

import android.annotation.TargetApi;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

public interface IClipboard {

    void setPrimaryClip(ClipData clip, String callingPackage, String attributionTag, int userId);

    void setPrimaryClipAsPackage(ClipData clip, String callingPackage, String attributionTag, int userId, String sourcePackage);

    void clearPrimaryClip(String callingPackage, String attributionTag, int userId);

    ClipData getPrimaryClip(String pkg, String attributionTag, int userId);

    ClipDescription getPrimaryClipDescription(String callingPackage, String attributionTag, int userId);

    boolean hasPrimaryClip(String callingPackage, String attributionTag, int userId);

    void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, String attributionTag, int userId);

    void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, String attributionTag, int userId);

    boolean hasClipboardText(String callingPackage, String attributionTag, int userId);

    String getPrimaryClipSource(String callingPackage, String attributionTag, int userId);


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    void setPrimaryClip(ClipData clip, String callingPackage, String attributionTag, int userId, int deviceId);

    //@EnforcePermission("SET_CLIP_SOURCE")
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    void setPrimaryClipAsPackage(ClipData clip, String callingPackage, String attributionTag, int userId, int deviceId, String sourcePackage);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    void clearPrimaryClip(String callingPackage, String attributionTag, int userId, int deviceId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    ClipData getPrimaryClip(String pkg, String attributionTag, int userId, int deviceId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    ClipDescription getPrimaryClipDescription(String callingPackage, String attributionTag, int userId, int deviceId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    boolean hasPrimaryClip(String callingPackage, String attributionTag, int userId, int deviceId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, String attributionTag, int userId, int deviceId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, String attributionTag, int userId, int deviceId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    boolean hasClipboardText(String callingPackage, String attributionTag, int userId, int deviceId);

    //@EnforcePermission("SET_CLIP_SOURCE")
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    String getPrimaryClipSource(String callingPackage, String attributionTag, int userId, int deviceId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    boolean areClipboardAccessNotificationsEnabledForUser(int userId);

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    void setClipboardAccessNotificationsEnabledForUser(boolean enable, int userId);


    abstract class Stub extends Binder implements IClipboard {
        public static IClipboard asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}