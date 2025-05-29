package android.content;

import android.annotation.TargetApi;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

public interface IClipboard {


    void setPrimaryClip(ClipData clip, String callingPackage);

    @RequiresApi(Build.VERSION_CODES.P)
    void clearPrimaryClip(String callingPackage);

    ClipData getPrimaryClip(String pkg);

    ClipDescription getPrimaryClipDescription(String callingPackage);

    boolean hasPrimaryClip(String callingPackage);

    void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener listener,
                                       String callingPackage);

    void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener listener);

    boolean hasClipboardText(String callingPackage);


    @RequiresApi(Build.VERSION_CODES.Q)
    void setPrimaryClip(ClipData clip, String callingPackage, int userId);

    @RequiresApi(Build.VERSION_CODES.Q)
    void clearPrimaryClip(String callingPackage, int userId);

    @RequiresApi(Build.VERSION_CODES.Q)
    ClipData getPrimaryClip(String pkg, int userId);

    @RequiresApi(Build.VERSION_CODES.Q)
    ClipDescription getPrimaryClipDescription(String callingPackage, int userId);

    @RequiresApi(Build.VERSION_CODES.Q)
    boolean hasPrimaryClip(String callingPackage, int userId);

    @RequiresApi(Build.VERSION_CODES.Q)
    void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener listener,
                                       String callingPackage, int userId);

    @RequiresApi(Build.VERSION_CODES.Q)
    void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, int userId);

    @RequiresApi(Build.VERSION_CODES.Q)
    boolean hasClipboardText(String callingPackage, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    void setPrimaryClip(ClipData clip, String callingPackage, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    void setPrimaryClipAsPackage(ClipData clip, String callingPackage, String attributionTag, int userId, String sourcePackage);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    void clearPrimaryClip(String callingPackage, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    ClipData getPrimaryClip(String pkg, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    ClipDescription getPrimaryClipDescription(String callingPackage, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    boolean hasPrimaryClip(String callingPackage, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    boolean hasClipboardText(String callingPackage, String attributionTag, int userId);

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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