package com.john.freezeapp.home;

import android.app.AppOpsManager;
import android.app.AppOpsManagerHidden;
import android.app.smartspace.ISmartspaceManager;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceSessionId;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.app.smartspace.uitemplatedata.BaseTemplateData;
import android.app.smartspace.uitemplatedata.Text;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;

import com.android.internal.app.IBatteryStats;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.CommandActivity;
import com.john.freezeapp.MainActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.appops.AppOps;
import com.john.freezeapp.appops.AppOpsActivity;
import com.john.freezeapp.battery.BatteryUsageActivity;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.clipboard.ClipboardActivity;
import com.john.freezeapp.daemon.DaemonBinderManager;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.util.SharedPreferencesImpl;
import com.john.freezeapp.deviceidle.DeviceIdleActivity;
import com.john.freezeapp.freeze.ManagerActivity;
import com.john.freezeapp.fs.ClientFileServer;
import com.john.freezeapp.fs.FileServerActivity;
import com.john.freezeapp.hyper.MiMixFlipSettingActivity;
import com.john.freezeapp.monitor.AppMonitorActivity;
import com.john.freezeapp.storage.StorageActivity;
import com.john.freezeapp.usagestats.UsageStatsActivity;
import com.john.freezeapp.usagestats.appstandby.AppStandbyActivity;
import com.john.freezeapp.util.DeviceUtil;
import com.john.freezeapp.util.FreezeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FreezeHomeToolHelper {
    public static List<FreezeHomeToolData> getFreezeHomeFuncData(Context context) {
        if (!ClientBinderManager.isActive()) {
            return null;
        }

        List<FreezeHomeToolData> list = new ArrayList<>();
        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_manager_app),
                R.drawable.ic_vector_apps,
                0xffF2C8F8,
                v -> {
                    Intent intent = new Intent(context, ManagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));

        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_app_usage),
                R.drawable.ic_vector_usage_stats,
                0xffACD4EB,
                v -> {
                    Intent intent = new Intent(context, UsageStatsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));

        IBatteryStats batteryStats = ClientSystemService.getBatteryStats();
        if (batteryStats != null && DeviceUtil.atLeast31()) {
            list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_battery_usage),
                    R.drawable.ic_vector_battery,
                    0xffB5EBDA,
                    v -> {
                        Intent intent = new Intent(context, BatteryUsageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }));
        }

        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_app_ops_name),
                R.drawable.ic_vector_key,
                0xffC1B790,
                v -> {
                    Intent intent = new Intent(context, AppOpsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));

        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_app_monitor),
                R.drawable.ic_vector_window,
                0xffEFAAB1,
                v -> {
                    Intent intent = new Intent(context, AppMonitorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));

        if (ClientBinderManager.isActive()) {
            if (TextUtils.equals("Xiaomi MIX Flip", ClientBinderManager.getConfig(DaemonHelper.DAEMON_MODULE_SYSTEM_PROPERTIES, "ro.product.marketname"))) {
                list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_mi_flip_setting),
                        R.drawable.ic_vector_display,
                        0xff9986A4,
                        v -> {
                            Intent intent = new Intent(context, MiMixFlipSettingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }));
            }
        }


        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_storage_name),
                R.drawable.ic_vector_storage,
                0xffBC9DEB,
                v -> {
                    Intent intent = new Intent(context, StorageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));


        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_clipboard_name),
                R.drawable.ic_vector_storage,
                0xffBC9DEB,
                v -> {
                    Intent intent = new Intent(context, ClipboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));

        if (DeviceUtil.atLeast28()) {
            list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_app_standby_bucket),
                    R.drawable.ic_vector_white_terminal,
                    0xff9986A4,
                    v -> {
                        Intent intent = new Intent(context, AppStandbyActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }));
        }


        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_app_device_idle),
                R.drawable.ic_vector_white_terminal,
                0xff9986A4,
                v -> {
                    Intent intent = new Intent(context, DeviceIdleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));



        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_app_file_server),
                R.drawable.ic_vector_white_terminal,
                0xff9986A4,
                v -> {
                    Intent intent = new Intent(context, FileServerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));

        list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_command_app),
                R.drawable.ic_vector_white_terminal,
                0xffF4E1B9,
                v -> {
                    Intent intent = new Intent(context, CommandActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }));


        if (BuildConfig.DEBUG) {
            list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_test),
                    R.drawable.ic_vector_window,
                    0xffD0ACA3,
                    v -> toTest(context)));



            list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_test2),
                    R.drawable.ic_vector_window,
                    0xffD0ACA3,
                    v -> toTest2(context)));

            list.add(new FreezeHomeToolData(context.getResources().getString(R.string.main_test2),
                    R.drawable.ic_vector_window,
                    0xffD0ACA3,
                    v -> toTest3(context)));
        }

        return list;
    }


    private static void toTest3(Context context) {

    }

    /**
     * smartspace
     *
     * @param context
     */
    private static void toTest2(Context context) {

    }


    /**
     * usagestats
     *
     * @param context
     */
    private static void toTest(Context context) {

    }
}
