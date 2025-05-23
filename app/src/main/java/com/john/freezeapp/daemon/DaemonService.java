package com.john.freezeapp.daemon;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.Context;
import android.content.IClipboard;
import android.content.pm.ILauncherApps;
import android.content.pm.IPackageManager;
import android.hardware.display.IDisplayManager;
import android.os.IBatteryPropertiesRegistrar;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.IUserManager;
import android.os.ServiceManager;
import android.permission.IPermissionManager;
import android.view.IWindowManager;

import com.android.internal.app.IAppOpsService;
import com.john.freezeapp.util.FreezeUtil;

public class DaemonService {
    private static final IAppOpsService appOps;
    private static final IActivityManager activityManager;
    private static final IUserManager userManager;
    private static final IPackageManager packageManager;
    private static final IPermissionManager permissionManager;
    private static final IDeviceIdleController deviceIdleController;
    private static final IDisplayManager displayManager;
    private static final IBatteryPropertiesRegistrar batteryPropertiesRegistrar;
    private static final ILauncherApps launcherApps;
    private static final IWindowManager windowManager;
    private static final IClipboard iClipboard;


    public static IActivityManager getActivityManager() {
        return activityManager;
    }

    public static IPackageManager getPackageManager() {
        return packageManager;
    }

    public static IClipboard getClipboard() {
        return iClipboard;
    }

    public static IAppOpsService getAppOps() {
        return appOps;
    }

    static {
        appOps = IAppOpsService.Stub.asInterface(ServiceManager.getService("appops"));

        IBinder service = ServiceManager.getService("activity");
        if (FreezeUtil.atLeast26()) {
            activityManager = IActivityManager.Stub.asInterface(service);
        } else {
            activityManager = ActivityManagerNative.asInterface(service);
        }

        userManager = IUserManager.Stub.asInterface(ServiceManager.getService("user"));

        packageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));


        if (FreezeUtil.atLeast30()) {
            permissionManager = IPermissionManager.Stub.asInterface(ServiceManager.getService("permissionmgr"));
        } else {
            permissionManager = null;
        }
        deviceIdleController = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
        displayManager = IDisplayManager.Stub.asInterface(ServiceManager.getService("display"));
        batteryPropertiesRegistrar = IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getService("batteryproperties"));
        launcherApps = ILauncherApps.Stub.asInterface(ServiceManager.getService("launcherapps"));
        windowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));

        iClipboard = IClipboard.Stub.asInterface(ServiceManager.getService(Context.CLIPBOARD_SERVICE));
    }
}
