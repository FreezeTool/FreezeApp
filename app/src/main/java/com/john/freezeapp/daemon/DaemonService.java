package com.john.freezeapp.daemon;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.Service;
import android.content.pm.ILauncherApps;
import android.content.pm.IPackageManager;
import android.hardware.display.IDisplayManager;
import android.os.Build;
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
    public static final IAppOpsService appOps;
    public static final IActivityManager activityManager;
    public static final IUserManager userManager;
    public static final IPackageManager packageManager;
    public static final IPermissionManager permissionManager;
    public static final IDeviceIdleController deviceIdleController;
    public static final IDisplayManager displayManager;
    public static final IBatteryPropertiesRegistrar batteryPropertiesRegistrar;
    public static final ILauncherApps launcherApps;
    public static final IWindowManager windowManager;


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
        if (FreezeUtil.atLeast30()) {
            deviceIdleController = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
        } else {
            deviceIdleController = null;
        }
        displayManager = IDisplayManager.Stub.asInterface(ServiceManager.getService("display"));
        batteryPropertiesRegistrar = IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getService("batteryproperties"));
        launcherApps = ILauncherApps.Stub.asInterface(ServiceManager.getService("launcherapps"));
        windowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
    }
}
