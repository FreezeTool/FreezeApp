package android.content.pm;

import android.app.IApplicationThread;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.IntentSender;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;

import androidx.annotation.RequiresApi;

import com.android.internal.infra.AndroidFuture;

import java.util.List;

public interface ILauncherApps extends IInterface {

    default void addOnAppsChangedListener(IOnAppsChangedListener listener)
            throws RemoteException {
        throw new RuntimeException();
    }

    @RequiresApi(Build.VERSION_CODES.N)
    default void addOnAppsChangedListener(String callingPackage, IOnAppsChangedListener listener)
            throws RemoteException {
        throw new RuntimeException();
    }

    default void removeOnAppsChangedListener(IOnAppsChangedListener listener)
            throws RemoteException {
        throw new RuntimeException();
    }

    @RequiresApi(Build.VERSION_CODES.N)
    default boolean startShortcut(String callingPackage, String packageName, String id,
                          Rect sourceBounds, Bundle startActivityOptions, int userId)
            throws RemoteException {
        throw new RuntimeException();
    }

    @RequiresApi(Build.VERSION_CODES.R)
    default boolean startShortcut(String callingPackage, String packageName, String featureId, String id,
                          Rect sourceBounds, Bundle startActivityOptions, int userId)
            throws RemoteException {
        throw new RuntimeException();
    }

    ParceledListSlice getLauncherActivities(
            String callingPackage, String packageName, UserHandle user);
    LauncherActivityInfoInternal resolveLauncherActivityInternal(
            String callingPackage, ComponentName component, UserHandle user);
    void startSessionDetailsActivityAsUser(IApplicationThread caller, String callingPackage,
                                           String callingFeatureId, PackageInstaller.SessionInfo sessionInfo,
                                           Rect sourceBounds, Bundle opts, UserHandle user);
    void startActivityAsUser(IApplicationThread caller, String callingPackage,
                             String callingFeatureId, ComponentName component, Rect sourceBounds,
                             Bundle opts, UserHandle user);
    PendingIntent getActivityLaunchIntent(String callingPackage, ComponentName component,
                                          UserHandle user);
    void showAppDetailsAsUser(IApplicationThread caller, String callingPackage,
                              String callingFeatureId, ComponentName component, Rect sourceBounds,
                              Bundle opts, UserHandle user);
    boolean isPackageEnabled(String callingPackage, String packageName, UserHandle user);
    Bundle getSuspendedPackageLauncherExtras(String packageName, UserHandle user);
    boolean isActivityEnabled(
            String callingPackage, ComponentName component, UserHandle user);
    ApplicationInfo getApplicationInfo(
            String callingPackage, String packageName, int flags, UserHandle user);

    LauncherAppsHidden.AppUsageLimit getAppUsageLimit(String callingPackage, String packageName,
                                                UserHandle user);

    ParceledListSlice getShortcuts(String callingPackage, ShortcutQueryWrapper query,
                                   UserHandle user);
    void getShortcutsAsync(String callingPackage, ShortcutQueryWrapper query,
                           UserHandle user, AndroidFuture<List<ShortcutInfo>> cb);
    void pinShortcuts(String callingPackage, String packageName, List<String> shortcutIds,
                      UserHandle user);

    int getShortcutIconResId(String callingPackage, String packageName, String id,
                             int userId);
    ParcelFileDescriptor getShortcutIconFd(String callingPackage, String packageName, String id,
                                           int userId);

    boolean hasShortcutHostPermission(String callingPackage);
    boolean shouldHideFromSuggestions(String packageName, UserHandle user);

    ParceledListSlice getShortcutConfigActivities(
            String callingPackage, String packageName, UserHandle user);

    IntentSender getShortcutConfigActivityIntent(String callingPackage, ComponentName component,
                                                 UserHandle user);
    PendingIntent getShortcutIntent(String callingPackage, String packageName, String shortcutId,
                                    Bundle opts, UserHandle user);

    void registerPackageInstallerCallback(String callingPackage,
                                          IPackageInstallerCallback callback);
    ParceledListSlice getAllSessions(String callingPackage);

    void registerShortcutChangeCallback(String callingPackage, ShortcutQueryWrapper query,
                                        IShortcutChangeCallback callback);
    void unregisterShortcutChangeCallback(String callingPackage,
                                          IShortcutChangeCallback callback);

    void cacheShortcuts(String callingPackage, String packageName, List<String> shortcutIds,
                        UserHandle user, int cacheFlags);
    void uncacheShortcuts(String callingPackage, String packageName, List<String> shortcutIds,
                          UserHandle user, int cacheFlags);

    String getShortcutIconUri(String callingPackage, String packageName, String shortcutId,
                              int userId);

    abstract class Stub extends Binder implements ILauncherApps {

        public static ILauncherApps asInterface(IBinder obj) {
            throw new RuntimeException();
        }

        @Override
        public IBinder asBinder() {
            throw new RuntimeException();
        }
    }
}
