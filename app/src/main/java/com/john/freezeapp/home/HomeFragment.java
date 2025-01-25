package com.john.freezeapp.home;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseFragment;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.DaemonStartActivity;
import com.john.freezeapp.FreezeAppManager;
import com.john.freezeapp.FreezeUtil;
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.R;
import com.john.freezeapp.SharedPrefUtil;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;
import com.john.freezeapp.daemon.DaemonHelper;

import java.util.ArrayList;
import java.util.List;

import rikka.shizuku.Shizuku;

public class HomeFragment extends BaseFragment {


    FreezeHomeAdapter homeAdapter = new FreezeHomeAdapter();


    static final int REQUEST_CODE = 124;

    private final Shizuku.OnRequestPermissionResultListener onRequestPermissionResultListener = new Shizuku.OnRequestPermissionResultListener() {
        @Override
        public void onRequestPermissionResult(int requestCode, int grantResult) {
            if (requestCode == REQUEST_CODE && grantResult == PackageManager.PERMISSION_GRANTED) {
                toRealShizuku();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestKernelVersion();
        Shizuku.addRequestPermissionResultListener(onRequestPermissionResultListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(homeAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateData(getContext());
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
                            SharedPrefUtil.setString(SharedPrefUtil.KEY_KERNEL_VERSION, commandResult.successMsg.replace("\n", ""));
                        }
                        postUI(new Runnable() {
                            @Override
                            public void run() {
                                updateData(getContext());
                            }
                        });
                    }
                });
            }
        }
    }


    private void updateData(Context context) {
        ClientLog.log("checkUI ClientBinder isActive=" + ClientBinderManager.isActive());
        List<FreezeHomeData> list = new ArrayList<>();
        String version = "";
        String tip = "";
        String btn = "";
        if (isDaemonActive()) {

            try {
                String daemonVersion = getDaemonBinder().getConfig(DaemonHelper.KEY_DAEMON_VERSION);
                version = daemonVersion + "（" + BuildConfig.VERSION_NAME + "）";
                if (!TextUtils.equals(daemonVersion, BuildConfig.VERSION_NAME)) {
                    tip = context.getString(R.string.main_home_daemon_tip);
                    btn = context.getString(R.string.main_restart_app_process);
                }
            } catch (RemoteException e) {
                version = BuildConfig.VERSION_NAME;
            }
        }
        FreezeHomeBillboardData freezeDaemonData = new FreezeHomeBillboardData();
        freezeDaemonData.isActive = isDaemonActive();
        freezeDaemonData.version = version;
        freezeDaemonData.tip = tip;
        freezeDaemonData.btn = btn;
        freezeDaemonData.onClickStartDaemon = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartDaemon();
            }
        };
        list.add(freezeDaemonData);

        list.add(getDeviceData());

        FreezeHomeDaemonData adbDaemonData = new FreezeHomeDaemonData();
        adbDaemonData.title = context.getString(R.string.main_adb_start_server_title);
        StringBuilder content = new StringBuilder();
        content.append(context.getString(R.string.main_adb_start_server_content));
        adbDaemonData.content = content.toString();
        adbDaemonData.icon = R.mipmap.ic_terminal;
        adbDaemonData.btnText = context.getString(R.string.main_start_watch_cmd);
        adbDaemonData.showBtn = !isDaemonActive();
        adbDaemonData.btnLeftDrawable = R.drawable.ic_vector_code;
        adbDaemonData.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWatchCommandDialog();
            }
        };

        list.add(adbDaemonData);

        FreezeHomeDaemonData shizukuDaemonData = new FreezeHomeDaemonData();
        boolean shizukuActive = FreezeUtil.isShizukuActive();
        shizukuDaemonData.title = context.getString(R.string.main_shizuku_start_server_title, context.getString(shizukuActive ? R.string.main_server_active : R.string.main_server_not_active));
        shizukuDaemonData.content = context.getString(R.string.main_shizuku_start_server_content);
        shizukuDaemonData.icon = R.mipmap.ic_server;
        shizukuDaemonData.btnText = context.getString(R.string.main_start_app_process);
        shizukuDaemonData.showBtn = !isDaemonActive() && shizukuActive;
        shizukuDaemonData.btnLeftDrawable = R.drawable.ic_vector_start;
        shizukuDaemonData.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toShizuku(v);
            }
        };

        list.add(shizukuDaemonData);
        boolean suEnable = FreezeUtil.isSuEnable();
        FreezeHomeDaemonData rootDaemonData = new FreezeHomeDaemonData();
        rootDaemonData.title = context.getString(R.string.main_root_start_server_title, context.getString(suEnable ? R.string.main_root_server_active : R.string.main_root_server_not_active));
        rootDaemonData.content = context.getString(R.string.main_root_start_server_content);
        rootDaemonData.icon = R.mipmap.ic_root;
        rootDaemonData.btnText = context.getString(R.string.main_start_app_process);
        rootDaemonData.showBtn = !isDaemonActive() && suEnable;
        rootDaemonData.btnLeftDrawable = R.drawable.ic_vector_start;
        rootDaemonData.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRoot(v);
            }
        };
        list.add(rootDaemonData);

        homeAdapter.updateData(list);
    }

    private void restartDaemon() {
        showLoading();
        String cmd = "sh " + FreezeUtil.getShellFilePath(getContext());
        ClientRemoteShell.execCommand(cmd, new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                hideLoading();
            }
        });
    }

    private void toRoot(View v) {
        showLoading();
        hideLoading(2000);
        FreezeAppManager.toRoot(getContext());
    }

    private FreezeHomeDeviceData getDeviceData() {
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
    protected void bindDaemon(IDaemonBinder daemonBinder) {
        super.bindDaemon(daemonBinder);
        forceHideLoading();
        updateData(getContext());
        requestKernelVersion();
    }


    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        forceHideLoading();
        updateData(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Shizuku.removeRequestPermissionResultListener(onRequestPermissionResultListener);
    }

    private void showWatchCommandDialog() {
        String command = String.format("$ adb shell sh %s", FreezeUtil.getShellFilePath(getContext()));
        new AlertDialog.Builder(getContext())
                .setMessage(command)
                .setPositiveButton(R.string.btn_copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(command);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


    public void toShizuku(View v) {

        if (!FreezeUtil.checkShizukuPermission()) {
            Shizuku.requestPermission(REQUEST_CODE);
        } else {
            toRealShizuku();
        }
    }

    private void toRealShizuku() {
        showLoading();
        FreezeAppManager.execShizuku(getContext(), null);
    }
}
