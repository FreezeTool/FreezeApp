package com.john.freezeapp;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.john.freezeapp.daemon.DaemonShellUtils;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ThreadPool;

import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;


public class ShizukuUtil {


    public static void execShizuku(Context context, ShizukuCallback callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String[] arrays = {"sh"};
                ShizukuRemoteProcess shizukuRemoteProcess = Shizuku.newProcess(arrays, null, null);
                DataOutputStream os = null;
                try {
                    os = new DataOutputStream(shizukuRemoteProcess.getOutputStream());
                    os.write(FreezeUtil.getStartShell(context).getBytes());
                    os.writeBytes("\n");
                    os.flush();
                    os.writeBytes("exit\n");
                    os.flush();
                    boolean result = DaemonShellUtils.waitFor(shizukuRemoteProcess, 1, TimeUnit.SECONDS);
                    if (callback != null) {
                        callback.success();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.fail();
                    }
                } finally {
                    try {
                        os.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static boolean isShizukuActive() {
        try {
            return Shizuku.getBinder() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEnable() {
        return true;
    }

    public static boolean checkShizukuPermission() {
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static void requestPermission(int requestCode) {
        Shizuku.requestPermission(requestCode);
    }

    private static Shizuku.OnRequestPermissionResultListener sOnRequestPermissionResultListener;

    public static void addRequestPermissionResultListener(OnRequestPermissionResultListener listener) {
        Shizuku.removeRequestPermissionResultListener(sOnRequestPermissionResultListener);
        sOnRequestPermissionResultListener = (requestCode, grantResult) -> listener.onRequestPermissionResult(requestCode, grantResult);

        Shizuku.addRequestPermissionResultListener(sOnRequestPermissionResultListener);
    }

    public static void removeRequestPermissionResultListener() {
        Shizuku.addRequestPermissionResultListener(sOnRequestPermissionResultListener);
    }
}
