package com.john.freezeapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.battery.BatteryUsageActivity;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.DaemonShellUtils;
import com.john.freezeapp.freeze.ManagerActivity;
import com.john.freezeapp.home.FreezeHomeAdapter;
import com.john.freezeapp.home.FreezeHomeFuncData;
import com.john.freezeapp.home.FreezeHomeFuncHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

public class MainActivity extends BaseActivity {

    TextView tvContent, tvServer, tvShizukuStatus, tvShizukuStartServer, tvRootStatus, tvRootStartServer;
    LinearLayout llStartServer;
    RecyclerView llActiveServer;
    Toolbar toolbar;

    FreezeHomeAdapter homeAdapter = new FreezeHomeAdapter();

    static final int REQUEST_CODE = 123;

    private final Shizuku.OnRequestPermissionResultListener onRequestPermissionResultListener = new Shizuku.OnRequestPermissionResultListener() {
        @Override
        public void onRequestPermissionResult(int requestCode, int grantResult) {
            if (requestCode == REQUEST_CODE && grantResult == PackageManager.PERMISSION_GRANTED) {
                toRealShizuku();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvContent = findViewById(R.id.tv_content);
        tvServer = findViewById(R.id.tv_server);
        llStartServer = findViewById(R.id.ll_start_server);
        llActiveServer = findViewById(R.id.ll_active_server);
        tvShizukuStatus = findViewById(R.id.tv_shizuku_status);
        tvShizukuStartServer = findViewById(R.id.tv_shizuku_start_server);
        tvRootStatus = findViewById(R.id.tv_root_status);
        tvRootStartServer = findViewById(R.id.tv_root_start_server);
        generateShell();
        initRoot();
        checkUI();
        initHomeFuncAdapter();
    }

    private void initHomeFuncAdapter() {
        llActiveServer.setLayoutManager(new LinearLayoutManager(this));
        llActiveServer.setAdapter(homeAdapter);
        homeAdapter.updateData(FreezeHomeFuncHelper.getFreezeHomeFuncData(this));
    }

    private void checkUI() {
        ClientLog.log("checkUI ClientBinder isActive=" + ClientBinderManager.isActive());
        if (isDaemonActive()) {
            showServerRunning("");
        } else {
            showServerUnRunning();
            showGuide();
        }
    }

    @Override
    protected void bindDaemon(IDaemonBinderContainer daemonBinderContainer) {
        super.bindDaemon(daemonBinderContainer);
        checkUI();
        removeHideLoading();
        hideLoading();
    }


    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        checkUI();
        removeHideLoading();
        hideLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHideLoading();
        Shizuku.removeRequestPermissionResultListener(onRequestPermissionResultListener);
    }

    private void initShizuku() {
        Shizuku.addRequestPermissionResultListener(onRequestPermissionResultListener);
        if (isShizukuActive()) {
            tvShizukuStatus.setText(getResources().getString(R.string.main_shizuku_server_active));
            tvShizukuStartServer.setVisibility(View.VISIBLE);
        } else {
            tvShizukuStatus.setText(getResources().getString(R.string.main_shizuku_server_not_active));
            tvShizukuStartServer.setVisibility(View.GONE);
        }
    }

    private void initRoot() {
        if (isSuEnable()) {
            tvRootStatus.setText(getResources().getString(R.string.main_root_server_active));
            tvRootStartServer.setVisibility(View.VISIBLE);
        } else {
            tvRootStatus.setText(getResources().getString(R.string.main_root_server_not_active));
            tvRootStartServer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initShizuku();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (R.id.menu_stop_server == itemId) {
            stopDaemon();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stopDaemon() {
        if (ClientBinderManager.isActive()) {
            try {
                ClientBinderManager.getDaemonBinderContainer().closeDeamon();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getShellFilePath() {
        File externalFilesDir = getExternalFilesDir(null);
        externalFilesDir.mkdirs();
        File file = new File(externalFilesDir.getAbsolutePath() + "/start.sh");
        return file.getAbsolutePath();
    }

    private String getStartShell() {
        return String.format("nohup app_process -Djava.class.path=%s /system/bin --nice-name=%s %s %s > /dev/null 2>&1 &",
                getApplicationInfo().sourceDir,
                DaemonHelper.DAEMON_NICKNAME,
                Daemon.class.getName(),
                getPackageName());
    }

    private void generateShell() {
        String shellFilePath = getShellFilePath();
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(shellFilePath);
            printWriter.println(getStartShell());
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

    public void showGuide() {
        llStartServer.setVisibility(View.VISIBLE);
        String shellFilePath = getShellFilePath();
        tvContent.setText(String.format("$ adb shell sh %s", shellFilePath));
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(shellFilePath);
            }
        });

    }

    private boolean checkShizukuPermission() {
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void toRealShizuku() {
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] arrays = {"sh"};
                ShizukuRemoteProcess shizukuRemoteProcess = Shizuku.newProcess(arrays, null, null);
                DataOutputStream os = null;
                try {
                    os = new DataOutputStream(shizukuRemoteProcess.getOutputStream());
                    os.write(getStartShell().getBytes());
                    os.writeBytes("\n");
                    os.flush();
                    os.writeBytes("exit\n");
                    os.flush();
                    boolean result = DaemonShellUtils.waitFor(shizukuRemoteProcess, 1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        os.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private boolean isShizukuActive() {
        try {
            return Shizuku.getBinder() != null;
        } catch (Exception e) {
            return false;
        }
    }


    public void toShizuku(View v) {
        Log.d("song", "");
        if (!checkShizukuPermission()) {
            Shizuku.requestPermission(REQUEST_CODE);
        } else {
            toRealShizuku();
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


    private void showServerUnRunning() {
        tvServer.setVisibility(View.VISIBLE);
        tvServer.setText(R.string.main_app_server_not_active);
        llActiveServer.setVisibility(View.GONE);
        llStartServer.setVisibility(View.VISIBLE);
    }

    private void showServerRunning(String task) {
        tvServer.setVisibility(View.VISIBLE);
        tvServer.setText(getString(R.string.main_app_server_active) + " " + task);
        tvContent.setOnClickListener(null);
        llActiveServer.setVisibility(View.VISIBLE);
        llStartServer.setVisibility(View.GONE);
    }

    public void toRoot(View view) {
        showLoading();
        delayHideLoading();
        DaemonShellUtils.execCommand(getStartShell(), true, new DaemonShellUtils.ShellCommandResultCallback() {
            @Override
            public void callback(DaemonShellUtils.ShellCommandResult commandResult) {

            }
        });
    }


    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mHideLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            hideLoading();
        }
    };

    private void delayHideLoading() {
        mHandler.postDelayed(mHideLoadingRunnable, 2000);
    }

    private void removeHideLoading() {
        mHandler.removeCallbacks(mHideLoadingRunnable);
    }
}
