package com.john.freezeapp.daemon.util;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Process;

import com.google.gson.Gson;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.runas.RunAs;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DaemonUtil {
    public static String getDaemonPackageName() {
        String[] packagesNames;
        try {
            packagesNames = ActivityThread.getPackageManager().getPackagesForUid(Process.myUid());
            if (packagesNames != null && packagesNames.length > 0) {
                return packagesNames[0];
            }
        } catch (Exception e) {
            //
        }
        return DaemonHelper.DAEMON_SHELL_PACKAGE;
    }



    public static String getCallingPackageName() {

        if (android.system.Os.getuid() == 2000) {
            return DaemonHelper.DAEMON_SHELL_PACKAGE;
        }
        return BuildConfig.APPLICATION_ID;
    }


    public static String md5(String plainString) {
        String cipherString = null;
        try {
            // 获取实例
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 计算摘要
            byte[] cipherBytes = messageDigest.digest(plainString.getBytes(StandardCharsets.UTF_8));
            // 输出为16进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : cipherBytes) {
                sb.append(String.format("%02x", b));
            }
            cipherString = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherString;
    }

    private static final Gson sGson = new Gson();

    public static Gson getGson() {
        return sGson;
    }


    public static String getRunAsShell(Context context, String packageName) {
        return String.format("nohup app_process -Djava.class.path=%s /system/bin --nice-name=%s %s %s > /dev/null 2>&1 &",
                context.getApplicationInfo().sourceDir,
                DaemonHelper.FREEZE_APP_LABEL + packageName,
                RunAs.class.getName(),
                context.getPackageName());
    }
}
