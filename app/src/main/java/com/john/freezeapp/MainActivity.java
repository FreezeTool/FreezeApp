package com.john.freezeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;
import com.john.freezeapp.daemon.DaemonShellUtils;
import com.john.freezeapp.home.FreezeHomeDaemonData;
import com.john.freezeapp.home.FreezeHomeDeviceData;
import com.john.freezeapp.home.FreezeHomeAdapter;
import com.john.freezeapp.home.FreezeHomeFuncHelper;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {


    RecyclerView recyclerView;
    Toolbar toolbar;

    FreezeHomeAdapter homeAdapter = new FreezeHomeAdapter();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(homeAdapter);
        generateShell();
        updateData();
        requestKernelVersion();
    }

    private void requestKernelVersion() {
        if (isDaemonActive()) {
            // 内核
            String kernelVersion = SharedPrefUtil.getString(SharedPrefUtil.KEY_KERNEL_VERSION, null);
            if (kernelVersion == null) {
                ClientRemoteShell.execCommand("uname -r", new ClientRemoteShell.RemoteShellCommandResultCallback() {
                    @Override
                    public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                        if (commandResult.result && !TextUtils.isEmpty(commandResult.successMsg)) {
                            SharedPrefUtil.setString(SharedPrefUtil.KEY_KERNEL_VERSION, commandResult.successMsg.replace("\n",""));
                        }
                    }
                });
            }
        }
    }


    private void updateData() {
        ClientLog.log("checkUI ClientBinder isActive=" + ClientBinderManager.isActive());
        List list = new ArrayList();
        FreezeHomeDaemonData freezeDaemonData = new FreezeHomeDaemonData(isDaemonActive(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DaemonStartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        list.add(freezeDaemonData);

        list.add(getDeviceData());
        if (isDaemonActive()) {
            list.addAll(FreezeHomeFuncHelper.getFreezeHomeFuncData(this));
        }

        homeAdapter.updateData(list);
    }

    private Object getDeviceData() {
        FreezeHomeDeviceData freezeDeviceData = new FreezeHomeDeviceData();
        // 设备
        freezeDeviceData.add(new FreezeHomeDeviceData.DeviceInfo("设备", FreezeUtil.getDevice()));
        // 版本
        String androidVersion = Build.VERSION.PREVIEW_SDK_INT != 0 ? Build.VERSION.CODENAME : Build.VERSION.RELEASE;
        freezeDeviceData.add(new FreezeHomeDeviceData.DeviceInfo("版本", String.format("Android %s (%s)", androidVersion, Build.VERSION.SDK_INT)));
        // 架构
        freezeDeviceData.add(new FreezeHomeDeviceData.DeviceInfo("架构", Build.SUPPORTED_ABIS[0]));
        // User
        freezeDeviceData.add(new FreezeHomeDeviceData.DeviceInfo("USER", Build.USER));
        // Build号
        freezeDeviceData.add(new FreezeHomeDeviceData.DeviceInfo("Build号", Build.ID));
        // 内核
        String kernelVersion = SharedPrefUtil.getString(SharedPrefUtil.KEY_KERNEL_VERSION, null);
        if (!TextUtils.isEmpty(kernelVersion)) {
            freezeDeviceData.add(new FreezeHomeDeviceData.DeviceInfo("内核", kernelVersion));
        }

        return freezeDeviceData;
    }

    @Override
    protected void bindDaemon(IDaemonBinderContainer daemonBinderContainer) {
        super.bindDaemon(daemonBinderContainer);
        updateData();
        requestKernelVersion();
    }


    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        updateData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            showStopDaemonDialog();
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

    private void generateShell() {
        String shellFilePath = FreezeUtil.getShellFilePath(this);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(shellFilePath);
            printWriter.println(FreezeUtil.getStartShell(this));
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

    private void showStopDaemonDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.freeze_stop_daemon_title)
                .setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopDaemon();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
