package com.john.freezeapp;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.DaemonShellUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

public class MainActivity extends AppCompatActivity {

    TextView tvContent, tvServer, tvShizukuStatus, tvShizukuStartServer, tvRootStatus, tvRootStartServer, tvTest;
    LinearLayout llStartServer, llActiveServer;
    Toolbar toolbar;
    RelativeLayout loadingView;

    static final int REQUEST_CODE = 123;
    AtomicInteger loadingInteger = new AtomicInteger(0);

    private final Shizuku.OnRequestPermissionResultListener onRequestPermissionResultListener = new Shizuku.OnRequestPermissionResultListener() {
        @Override
        public void onRequestPermissionResult(int requestCode, int grantResult) {
            if (requestCode == REQUEST_CODE && grantResult == PackageManager.PERMISSION_GRANTED) {
                startFreezeByShizuku();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTest = findViewById(R.id.tv_test);
        tvTest.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvContent = findViewById(R.id.tv_content);
        tvServer = findViewById(R.id.tv_server);
        llStartServer = findViewById(R.id.ll_start_server);
        llActiveServer = findViewById(R.id.ll_active_server);
        loadingView = findViewById(R.id.loading);
        tvShizukuStatus = findViewById(R.id.tv_shizuku_status);
        tvShizukuStartServer = findViewById(R.id.tv_shizuku_start_server);
        tvRootStatus = findViewById(R.id.tv_root_status);
        tvRootStartServer = findViewById(R.id.tv_root_start_server);

        generateShell();
        initRoot();
        checkUI();
        initBinderContainerListener();
    }

    private void checkUI() {
        ClientLog.log("checkUI ClientBinder isActive=" + ClientBinderManager.isActive());
        if (ClientBinderManager.isActive()) {
            showServerRunning("");
        } else {
            showServerUnRunning();
            showGuide();
        }
    }

    private void initBinderContainerListener() {
        ClientBinderManager.registerDaemonBinderContainerListener(new ClientBinderManager.IDaemonBinderContainerListener() {
            @Override
            public void bind(IDaemonBinderContainer daemonBinderContainer) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkUI();
                    }
                });
            }

            @Override
            public void unbind() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkUI();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void toManager(View v) {
        Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void toCommand(View v) {
        Intent intent = new Intent(MainActivity.this, CommandActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void toTest(View v) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                List<ActivityManager.RunningTaskInfo> tasks = ClientBinderManager.iActivityManager.get().getTasks(10);
                for (ActivityManager.RunningTaskInfo task : tasks) {
                    ClientLog.log(task.toString());
                }
            }
        } catch (Throwable e) {
            //
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

    private void startFreezeByShizuku() {
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
            startFreezeByShizuku();
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


    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingInteger.getAndIncrement();
                loadingView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingInteger.getAndDecrement();
                if (loadingInteger.get() == 0) {
                    loadingView.setVisibility(View.GONE);
                }
            }
        });
    }


    public void toRoot(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaemonShellUtils.execCommand(getStartShell(), true, null);
            }
        }).start();
    }
}
