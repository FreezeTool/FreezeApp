package com.john.freezeapp.daemon.util;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Process;
import android.system.ErrnoException;
import android.system.Os;

import com.google.gson.Gson;
import com.john.freezeapp.daemon.BuildConfig;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.runas.RunAs;
import com.john.freezeapp.util.ThreadPool;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

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

        if (Os.getuid() == 2000) {
            return DaemonHelper.DAEMON_SHELL_PACKAGE;
        }
        return BuildConfig.CLIENT_PACKAGE;
    }

    public static String getShellPackageName() {
        return DaemonHelper.DAEMON_SHELL_PACKAGE;
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
                DaemonHelper.FREEZE_DAEMON_PREFIX + packageName,
                RunAs.class.getName(),
                context.getPackageName());
    }

    private static void setUid(int uid) {
        try {
            Os.setuid(uid);
        } catch (ErrnoException e) {
            e.printStackTrace();
        }
    }

    private synchronized static <T> T innerRunShellProcess(Callable<T> callable, long delay) {

        try {
            setUid(2000);
            T t = null;
            try {
                t = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (delay == 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return t;
        } finally {
            setUid(0);
        }
    }

    public synchronized static void syncRunShellProcess(Runnable runnable, long delay) {
        if (Os.getuid() == 0) {
            ThreadPool.execute(() -> innerRunShellProcess(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    runnable.run();
                    return null;
                }
            }, delay));
        } else {
            runnable.run();
        }
    }

    public synchronized static <T> T runShellProcess(Callable<T> callable) {
        if (Os.getuid() == 0) {
            return innerRunShellProcess(callable, 0);
        } else {
            try {
                return callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public synchronized static void runShellProcess(Runnable callable) {
        if (Os.getuid() == 0) {
            innerRunShellProcess(() -> {
                callable.run();
                return null;
            }, 0);
        } else {
            callable.run();
        }
    }


}
