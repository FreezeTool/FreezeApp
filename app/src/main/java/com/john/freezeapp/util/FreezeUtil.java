package com.john.freezeapp.util;

import android.app.AppOpsManager;
import android.app.AppOpsManagerHidden;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.CommonShellUtils;
import com.john.freezeapp.setting.SettingActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public static String getStartDaemonShell(Context context) {
        return CommonUtil.getAppProcessShell(context.getApplicationInfo().sourceDir, BuildConfig.DAEMON_CLASS_NAME, DaemonHelper.DAEMON_NICKNAME, context.getPackageName(), BuildConfig.DEBUG);
    }


    public static void generateShell(Context context) {
//        coppyDaemonApk(context);

        long freezeAppVersion = BuildConfig.FREEZEAPP_VERSION;

        // 获取本地存储的时间戳
        SharedPreferences sp = context.getSharedPreferences("freeze_config", Context.MODE_PRIVATE);
        long spFreezeAppVersion = sp.getLong("daemon_shell_version", 0);
        String shellFilePath = FreezeUtil.getShellFilePath(context);
        // 如果时间戳不一致，则复制资源
        if (freezeAppVersion != spFreezeAppVersion || !new File(shellFilePath).exists()) {
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(shellFilePath);
                printWriter.println(FreezeUtil.getStartDaemonShell(context));
                printWriter.println("sleep 1");
                printWriter.println("echo success");
                printWriter.close();
                CommonShellUtils.execCommand("chmod a+r " + shellFilePath, false, null);
            } catch (Throwable th) {
                try {
                    if (printWriter != null) {
                        printWriter.close();
                    }
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            sp.edit().putLong("daemon_shell_version", freezeAppVersion).apply();
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
        if (!DeviceUtil.atLeast26()) {
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


    public static void writeDaemonData(Context context) {
        try {
            // 读取 assets 中的 version.txt
            InputStream is = context.getAssets().open("version.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String buildTimestamp = reader.readLine();
            reader.close();
            is.close();

            // 获取本地存储的时间戳
            SharedPreferences sp = context.getSharedPreferences("freeze_config", Context.MODE_PRIVATE);
            String localTimestamp = sp.getString("build_timestamp", "");

            // 如果时间戳不一致，则复制资源
            if (!buildTimestamp.equals(localTimestamp)) {
                // 复制 sourceDir 文件到外部存储
                String sourceDir = context.getApplicationInfo().sourceDir;
                File sourceFile = new File(sourceDir);

                // 获取外部存储目录
                File externalDir = context.getExternalFilesDir(null);
                if (externalDir != null && externalDir.exists()) {
                    File destFile = new File(externalDir, "daemon.apk");

                    if (!destFile.exists() || destFile.lastModified() != sourceFile.lastModified()) {
                        FileUtils.copyFile(sourceFile, destFile);
                    }
                }

                // 更新本地存储的时间戳
                sp.edit().putString("build_timestamp", buildTimestamp).apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void coppyDaemonApk(Context context) {
        try {

            long freezeAppVersion = BuildConfig.FREEZEAPP_VERSION;

            // 获取本地存储的时间戳
            SharedPreferences sp = context.getSharedPreferences("freeze_config", Context.MODE_PRIVATE);
            long spFreezeAppVersion = sp.getLong("daemon_apk_version", 0);

            // 如果时间戳不一致，则复制资源
            if (freezeAppVersion != spFreezeAppVersion) {
                // 复制 daemon.apk 到外部存储
                InputStream apkIs = context.getAssets().open("daemon.apk");
                File externalDir = context.getExternalFilesDir(null);
                if (externalDir != null && externalDir.exists()) {
                    File destFile = new File(externalDir, "daemon.apk");
                    FileOutputStream fos = new FileOutputStream(destFile);

                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = apkIs.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    apkIs.close();

                    // 更新本地存储的时间戳
                    sp.edit().putLong("daemon_apk_version", freezeAppVersion).apply();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
