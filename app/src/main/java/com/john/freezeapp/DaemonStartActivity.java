package com.john.freezeapp;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.john.freezeapp.daemon.DaemonShellUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.util.concurrent.TimeUnit;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

public class DaemonStartActivity extends BaseActivity {
    Toolbar toolbar;
    TextView tvContent, tvShizukuStatus, tvShizukuStartServer, tvRootStatus, tvRootStartServer;

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
        if (isDaemonActive()) {
            finish();
        }
        setContentView(R.layout.activity_start_daemon);
        tvContent = findViewById(R.id.tv_adb_content);
        tvShizukuStatus = findViewById(R.id.tv_shizuku_content);
        tvShizukuStartServer = findViewById(R.id.tv_shizuku_start_server);
        tvRootStatus = findViewById(R.id.tv_root_content);
        tvRootStartServer = findViewById(R.id.tv_root_start_server);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRoot();
        initAdb();
    }

    private void initAdb() {
        String shellFilePath = FreezeUtil.getShellFilePath(this);
        tvContent.setText(String.format("$ adb shell sh %s", shellFilePath));
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(shellFilePath);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initShizuku();
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

    private boolean isShizukuActive() {
        try {
            return Shizuku.getBinder() != null;
        } catch (Exception e) {
            return false;
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 在这里处理返回按钮的点击事件
                finish(); // 或者其他你想要执行的操作
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void bindDaemon(IDaemonBinderContainer daemonBinderContainer) {
        super.bindDaemon(daemonBinderContainer);
        if (isDaemonActive()) {
            finish();
        }

        removeHideLoading();
        hideLoading();
    }


    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHideLoading();
        Shizuku.removeRequestPermissionResultListener(onRequestPermissionResultListener);
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
                    os.write(FreezeUtil.getStartShell(DaemonStartActivity.this).getBytes());
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


    public void toShizuku(View v) {
        Log.d("song", "");
        if (!checkShizukuPermission()) {
            Shizuku.requestPermission(REQUEST_CODE);
        } else {
            toRealShizuku();
        }
    }

    public void toRoot(View view) {
        showLoading();
        delayHideLoading();
        DaemonShellUtils.execCommand(FreezeUtil.getStartShell(this), true, new DaemonShellUtils.ShellCommandResultCallback() {
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
