package com.john.freezeapp.util;

import android.app.AppOpsManager;
import android.app.AppOpsManagerHidden;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.john.freezeapp.App;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.appops.AppOps;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.DaemonShellUtils;
import com.john.freezeapp.setting.SettingActivity;
import com.john.freezeapp.usagestats.appstandby.StandbyBucket;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rikka.shizuku.Shizuku;

public class FreezeUtil {
    public static String getDevice() {
        String manufacturer = Character.toUpperCase(Build.MANUFACTURER.charAt(0)) + Build.MANUFACTURER.substring(1);
        if (!Build.BRAND.equals(Build.MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(Build.BRAND.charAt(0)) + Build.BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        return manufacturer;
    }

    public static String getShellFilePath(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdirs();
        }
        File file = new File(externalFilesDir.getAbsolutePath() + "/start.sh");
        return file.getAbsolutePath();
    }

    public static String getStartShell(Context context) {
        String debugArgs = "";
        if (BuildConfig.DEBUG) {
            if (FreezeUtil.atLeast30()) {
                debugArgs = "-Xcompiler-option" + " --debuggable" +
                        " -XjdwpProvider:adbconnection" +
                        " -XjdwpOptions:suspend=n,server=y";
            } else if (FreezeUtil.atLeast28()) {
                debugArgs = "-Xcompiler-option" + " --debuggable" +
                        " -XjdwpProvider:internal" +
                        " -XjdwpOptions:transport=dt_android_adb,suspend=n,server=y";
            } else {
                debugArgs = "-Xcompiler-option" + " --debuggable" +
                        " -agentlib:jdwp=transport=dt_android_adb,suspend=n,server=y";
            }
        }
        return String.format("nohup app_process %s -Djava.class.path=%s /system/bin --nice-name=%s %s %s > /dev/null 2>&1 &",
                debugArgs,
                context.getApplicationInfo().sourceDir,
                DaemonHelper.DAEMON_NICKNAME,
                Daemon.class.getName(),
                context.getPackageName());
    }

    public static void generateShell(Context context) {
        String shellFilePath = FreezeUtil.getShellFilePath(context);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(shellFilePath);
            printWriter.println(FreezeUtil.getStartShell(context));
            printWriter.println("sleep 1");
            printWriter.println("echo success");
            printWriter.close();
            DaemonShellUtils.execCommand("chmod a+r " + shellFilePath, false, null);
        } catch (Throwable th) {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        }
    }

    public static void stopDaemon() {
        if (ClientBinderManager.isActive()) {
            try {
                ClientBinderManager.getDaemonBinder().closeDaemon();
            } catch (Throwable e) {
                //
            }
        }
    }


    public static boolean isSuEnable() {
        File file = null;
        String[] paths = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/", "/su/bin/"};
        try {
            for (String path : paths) {
                file = new File(path + "su");
                if (file.exists() && file.canExecute()) {
                    return true;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return false;
    }

    public static boolean isShizukuActive() {
        try {
            return Shizuku.getBinder() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkShizukuPermission() {
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    public static String listToString(List<String> list, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(list.get(i));
            if (i != length - 1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    public static void shellCommand(IBinder service, FileDescriptor in, FileDescriptor out,
                                    FileDescriptor err,
                                    String[] args, ShellCallback shellCallback,
                                    ResultReceiver resultReceiver) throws Exception {


    }

    public static boolean isFreezeApp(String packageName) {
        return TextUtils.equals(packageName, BuildConfig.APPLICATION_ID);
    }


    public static void toDevelopPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void toNotificationPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //
        }
    }

    public static void openFreezeAppSettings(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void allowSystemAlertWindow() {
        try {
            AppOps.setUidMode(AppOpsManagerHidden.OP_SYSTEM_ALERT_WINDOW, AppOpsManager.MODE_ALLOWED, Process.myUid(), BuildConfig.APPLICATION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toOverlayPermissionPage(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        context.startActivity(intent);
    }

    public static boolean isOverlayPermission(Context context) {
        if (!atLeast26()) {
            return true;
        }
        return Settings.canDrawOverlays(context);
    }

    public static void toWifiSettingPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

    public static void toPackageSetting(Context context, String packageName) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }


    public static boolean atLeast26() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean atLeast24() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean atLeast28() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean atLeast29() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static boolean atLeast30() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    public static boolean atLeast31() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    public static boolean atLeast33() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    public static boolean atLeast34() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE;
    }

    public static boolean atLeast35() {
        return Build.VERSION.SDK_INT >= 35;
    }


    public static String formatTime(long time) {
        StringBuilder stringBuilder = new StringBuilder();

        if (time > DateUtils.DAY_IN_MILLIS) {
            int day = (int) (time / DateUtils.DAY_IN_MILLIS);
            stringBuilder.append(day).append("天");
            time = time % DateUtils.DAY_IN_MILLIS;
        }

        if (time > DateUtils.HOUR_IN_MILLIS) {
            int hour = (int) (time / DateUtils.HOUR_IN_MILLIS);
            stringBuilder.append(hour).append("小时");
            time = time % DateUtils.HOUR_IN_MILLIS;
        }

        if (time > DateUtils.MINUTE_IN_MILLIS) {
            int minute = (int) (time / DateUtils.MINUTE_IN_MILLIS);
            stringBuilder.append(minute).append("分");
            time = time % DateUtils.MINUTE_IN_MILLIS;
        }

        if (time > DateUtils.SECOND_IN_MILLIS) {
            int second = (int) (time / DateUtils.SECOND_IN_MILLIS);
            stringBuilder.append(second).append("秒");
            time = time % DateUtils.SECOND_IN_MILLIS;
        }

        if (stringBuilder.toString().isEmpty()) {
            stringBuilder.append(time).append("毫秒");
        }
        return stringBuilder.toString();
    }


    public static String formatAppOpsTime(long now, long last) {
        long time = now - last;
        StringBuilder stringBuilder = new StringBuilder();

        if (time > 7 * DateUtils.DAY_IN_MILLIS) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(last);
            Calendar nowCalender = Calendar.getInstance();
            nowCalender.setTimeInMillis(now);
            int lastYear = calendar.get(Calendar.YEAR);
            int nowYear = nowCalender.get(Calendar.YEAR);
            if (lastYear != nowYear) {
                stringBuilder.append(lastYear).append("年");
            }
            stringBuilder.append(calendar.get(Calendar.MONTH) + 1).append("月").append(calendar.get(Calendar.DAY_OF_MONTH)).append("日");
        } else if (time > 2 * DateUtils.DAY_IN_MILLIS) {
            int day = (int) (time / DateUtils.DAY_IN_MILLIS);
            stringBuilder.append(day).append("天前");
        } else if (time > DateUtils.DAY_IN_MILLIS) {
            stringBuilder.append("昨天前");
        } else if (time > DateUtils.HOUR_IN_MILLIS) {
            int hour = (int) (time / DateUtils.HOUR_IN_MILLIS);
            stringBuilder.append(hour).append("小时前");
        } else if (time > DateUtils.MINUTE_IN_MILLIS) {
            int minute = (int) (time / DateUtils.MINUTE_IN_MILLIS);
            stringBuilder.append(minute).append("分前");
        } else if (time > DateUtils.SECOND_IN_MILLIS) {
            int second = (int) (time / DateUtils.SECOND_IN_MILLIS);
            stringBuilder.append(second).append("秒前");
        }


        return stringBuilder.toString();
    }


    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    private final static String[] UNITS = new String[]{"B", "KB", "MB", "GB", "TB"};


    public static String getSizeText(long cacheBytes) {
        if (cacheBytes <= 0) return "0B";
        int digitGroups = (int) (Math.log10(cacheBytes) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(cacheBytes / Math.pow(1024, digitGroups)) + " " + UNITS[digitGroups];
    }


    public static void showLongToast(String msg) {
        Toast.makeText(App.getApp(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String msg) {
        Toast.makeText(App.getApp(), msg, Toast.LENGTH_SHORT).show();
    }
}
