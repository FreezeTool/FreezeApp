package com.john.freezeapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.text.TextUtils;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.DaemonShellUtils;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        externalFilesDir.mkdirs();
        File file = new File(externalFilesDir.getAbsolutePath() + "/start.sh");
        return file.getAbsolutePath();
    }

    public static String getStartShell(Context context) {
        return String.format("nohup app_process -Djava.class.path=%s /system/bin --nice-name=%s %s %s > /dev/null 2>&1 &",
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
        Method shellCommand;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            shellCommand = HiddenApiBypass.getDeclaredMethod(service.getClass(), "shellCommand", FileDescriptor.class, FileDescriptor.class, FileDescriptor.class, String[].class, ShellCallback.class, ResultReceiver.class);
        } else {
            shellCommand = service.getClass().getDeclaredMethod("shellCommand", FileDescriptor.class, FileDescriptor.class, FileDescriptor.class, String[].class, ShellCallback.class, ResultReceiver.class);
        }
        shellCommand.invoke(service, in, out, err, args, shellCallback, resultReceiver);
    }

    public static boolean isFreezeApp(String packageName) {
        return TextUtils.equals(packageName, BuildConfig.APPLICATION_ID);
    }

}
