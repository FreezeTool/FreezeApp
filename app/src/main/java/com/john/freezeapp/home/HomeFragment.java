package com.john.freezeapp.home;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.system.Os;
import android.system.StructUtsname;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseFragment;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.R;
import com.john.freezeapp.adb.AdbPairActivity;
import com.john.freezeapp.adb.AdbStartDialog;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientDaemonService;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import rikka.shizuku.Shizuku;

public class HomeFragment extends BaseFragment {


    FreezeHomeAdapter homeAdapter = new FreezeHomeAdapter();

    private boolean isRoot = false;
    private boolean isShizuku = false;
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
        Shizuku.addRequestPermissionResultListener(onRequestPermissionResultListener);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_home_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (R.id.menu_stop_server == itemId) {
            showStopDaemonDialog();
            return true;
        } else if (R.id.menu_developer == itemId) {
            FreezeUtil.toDevelopPage(getContext());
            return true;
        } else if (R.id.menu_settings == itemId) {
            FreezeUtil.openFreezeAppSettings(getContext());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStopDaemonDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.freeze_stop_daemon_title)
                .setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FreezeUtil.stopDaemon();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(homeAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isRoot = FreezeUtil.isSuEnable();
        isShizuku = FreezeUtil.isShizukuActive();
        updateData(view.getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isRoot = FreezeUtil.isSuEnable();
        boolean isShizuku = FreezeUtil.isShizukuActive();

        if (this.isRoot != isRoot || this.isShizuku != isShizuku) {
            updateData(getContext());
        }
    }


    private void updateData(Context context) {

        if (!isAdded()) {
            return;
        }

        ClientLog.log("checkUI ClientBinder isActive=" + ClientBinderManager.isActive());
        List<FreezeHomeData> list = new ArrayList<>();


        FreezeHomeBillboardData freezeDaemonData = getFreezeHomeBillboardData(context);
        list.add(freezeDaemonData);

        list.add(getDeviceData());

        FreezeHomeDaemonData adbDaemonData = getFreezeHomeAdbData(context);
        FreezeHomeDaemonData shizukuDaemonData = getFreezeHomeShizukuData(context);
        FreezeHomeDaemonData rootDaemonData = getFreezeHomeRootData(context);
        FreezeHomeDaemonData wirelessAdbDaemonData = getFreezeHomeWirelessAdbData(context);

        List<FreezeHomeData> startDaemonData = new ArrayList<>();
        if (FreezeUtil.atLeast30()) {
            startDaemonData.add(wirelessAdbDaemonData);
        }

        startDaemonData.add(adbDaemonData);
        if (BuildConfig.DEBUG) {
            startDaemonData.add(shizukuDaemonData);
        }
        startDaemonData.add(rootDaemonData);

        if (this.isRoot && startDaemonData.contains(rootDaemonData)) {
            list.add(rootDaemonData);
            startDaemonData.remove(rootDaemonData);
        }

        if (this.isShizuku && startDaemonData.contains(shizukuDaemonData)) {
            list.add(shizukuDaemonData);
            startDaemonData.remove(shizukuDaemonData);
        }


        list.addAll(startDaemonData);


        homeAdapter.updateData(list);
    }

    private FreezeHomeDaemonData getFreezeHomeWirelessAdbData(Context context) {
        FreezeHomeDaemonData adbDaemonData = new FreezeHomeDaemonData();
        adbDaemonData.title = context.getString(R.string.main_wireless_adb_start_server_title);
        StringBuilder content = new StringBuilder();
        content.append(context.getString(R.string.main_wireless_adb_start_server_content));
        adbDaemonData.content = content.toString();
        adbDaemonData.icon = R.drawable.ic_vector_wifi;

        FreezeHomeDaemonData.DaemonBtnData rightBtnData = new FreezeHomeDaemonData.DaemonBtnData();
        rightBtnData.text = context.getString(R.string.main_start_app_process);
        rightBtnData.show = !isDaemonActive();
        rightBtnData.icon = R.drawable.ic_vector_start;
        rightBtnData.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wirelessAdb();
            }
        };
        adbDaemonData.rightDaemonBtnData = rightBtnData;

        FreezeHomeDaemonData.DaemonBtnData leftBtnData = new FreezeHomeDaemonData.DaemonBtnData();
        leftBtnData.text = context.getString(R.string.main_adb_pair);
        leftBtnData.show = !isDaemonActive();
        leftBtnData.icon = R.drawable.ic_vector_link;
        leftBtnData.onClickListener = v -> {
            toAdbPair(context);
        };


        adbDaemonData.leftDaemonBtnData = leftBtnData;
        return adbDaemonData;
    }

    private void wirelessAdb() {
        Context context = getContext();
        if (context != null) {
            AdbStartDialog adbStartDialog = new AdbStartDialog(context);
            adbStartDialog.show();
        }


    }

    private void toAdbPair(Context context) {
        Intent intent = new Intent(context, AdbPairActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private @NonNull FreezeHomeBillboardData getFreezeHomeBillboardData(Context context) {
        FreezeHomeBillboardData freezeDaemonData = new FreezeHomeBillboardData();
        String version = "";
        String tip = "";
        String btn = "";
        if (isDaemonActive()) {

            try {
                String daemonVersion = getDaemonBinder().getConfig(DaemonHelper.DAEMON_MODULE_CUSTOM, DaemonHelper.KEY_DAEMON_VERSION);
                version = daemonVersion + "（" + BuildConfig.VERSION_NAME + "）";
                if (!TextUtils.equals(daemonVersion, BuildConfig.VERSION_NAME)) {
                    tip = context.getString(R.string.main_home_daemon_tip);
                    btn = context.getString(R.string.main_restart_app_process);
                }
            } catch (RemoteException e) {
                version = BuildConfig.VERSION_NAME;
            }

            freezeDaemonData.rightInfo = String.format("PID\n%s", ClientDaemonService.getDaemonPid());
        } else {
            freezeDaemonData.rightInfo = "";
        }
        freezeDaemonData.isActive = isDaemonActive();
        freezeDaemonData.version = String.format("版本 %s , adb", version);
        freezeDaemonData.tip = tip;
        freezeDaemonData.btn = btn;
        freezeDaemonData.onClickStartDaemon = v -> restartDaemon();

        return freezeDaemonData;
    }

    private @NonNull FreezeHomeDaemonData getFreezeHomeRootData(Context context) {
        FreezeHomeDaemonData rootDaemonData = new FreezeHomeDaemonData();
        rootDaemonData.title = context.getString(R.string.main_root_start_server_title, context.getString(this.isRoot ? R.string.main_root_server_active : R.string.main_root_server_not_active));
        rootDaemonData.content = context.getString(R.string.main_root_start_server_content);
        rootDaemonData.icon = this.isRoot ? R.drawable.ic_vector_unlock : R.drawable.ic_vector_lock;

        FreezeHomeDaemonData.DaemonBtnData rightBtnData = new FreezeHomeDaemonData.DaemonBtnData();
        rightBtnData.text = context.getString(R.string.main_start_app_process);
        rightBtnData.show = !isDaemonActive() && this.isRoot;
        rightBtnData.icon = R.drawable.ic_vector_start;
        rightBtnData.onClickListener = this::toRoot;
        rootDaemonData.rightDaemonBtnData = rightBtnData;
        return rootDaemonData;
    }

    private @NonNull FreezeHomeDaemonData getFreezeHomeShizukuData(Context context) {
        FreezeHomeDaemonData shizukuDaemonData = new FreezeHomeDaemonData();
        shizukuDaemonData.title = context.getString(R.string.main_shizuku_start_server_title, context.getString(this.isShizuku ? R.string.main_server_active : R.string.main_server_not_active));
        shizukuDaemonData.content = context.getString(R.string.main_shizuku_start_server_content);
        shizukuDaemonData.icon = this.isShizuku ? R.drawable.ic_vector_domain : R.drawable.ic_vector_domain_disable;

        FreezeHomeDaemonData.DaemonBtnData rightBtnData = new FreezeHomeDaemonData.DaemonBtnData();
        rightBtnData.text = context.getString(R.string.main_start_app_process);
        rightBtnData.show = !isDaemonActive() && this.isShizuku;
        rightBtnData.icon = R.drawable.ic_vector_start;
        rightBtnData.onClickListener = this::toShizuku;
        shizukuDaemonData.rightDaemonBtnData = rightBtnData;
        return shizukuDaemonData;
    }

    private @NonNull FreezeHomeDaemonData getFreezeHomeAdbData(Context context) {
        FreezeHomeDaemonData adbDaemonData = new FreezeHomeDaemonData();
        adbDaemonData.title = context.getString(R.string.main_adb_start_server_title);
        StringBuilder content = new StringBuilder();
        content.append(context.getString(R.string.main_adb_start_server_content));
        adbDaemonData.content = content.toString();
        adbDaemonData.icon = R.drawable.ic_vector_terminal;

        FreezeHomeDaemonData.DaemonBtnData rightBtnData = new FreezeHomeDaemonData.DaemonBtnData();
        rightBtnData.text = context.getString(R.string.main_start_watch_cmd);
        rightBtnData.show = !isDaemonActive();
        rightBtnData.icon = R.drawable.ic_vector_code;
        rightBtnData.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWatchCommandDialog();
            }
        };
        adbDaemonData.rightDaemonBtnData = rightBtnData;
        return adbDaemonData;
    }

    private void restartDaemon() {
        Context context = getContext();
        if (context != null) {
            showLoading();
            String cmd = "sh " + FreezeUtil.getShellFilePath(context);
            ClientRemoteShell.execCommand(cmd, new ClientRemoteShell.RemoteShellCommandResultCallback() {
                @Override
                public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                    hideLoading();
                }
            });
        }
    }

    private void toRoot(View v) {
        Context context = getContext();
        if (context != null) {
            showLoading();
            hideLoading(2000);
            FreezeAppManager.toRoot(context);
        }
    }

    private FreezeHomeDeviceData getDeviceData() {
        FreezeHomeDeviceData freezeDeviceData = new FreezeHomeDeviceData();
        // 设备
        freezeDeviceData.add(new FreezeHomeDeviceInfoData("设备", FreezeUtil.getDevice()));
        // 版本
        String androidVersion = Build.VERSION.PREVIEW_SDK_INT != 0 ? Build.VERSION.CODENAME : Build.VERSION.RELEASE;
        freezeDeviceData.add(new FreezeHomeDeviceInfoData("版本", String.format("Android %s (%s)", androidVersion, Build.VERSION.SDK_INT)));
        // 架构
        freezeDeviceData.add(new FreezeHomeDeviceInfoData("架构", Build.SUPPORTED_ABIS[0]));
        // User
        freezeDeviceData.add(new FreezeHomeDeviceInfoData("USER", Build.USER));
        // Build号
        freezeDeviceData.add(new FreezeHomeDeviceInfoData("Build号", Build.ID));
        // 内核
        StructUtsname uname = Os.uname();
        if (uname != null) {
            freezeDeviceData.add(new FreezeHomeDeviceInfoData("内核", uname.release));
        }

        return freezeDeviceData;
    }

    @Override
    protected void bindDaemon(IDaemonBinder daemonBinder) {
        super.bindDaemon(daemonBinder);
        forceHideLoading();
        updateData(getContext());
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

        Context context = getContext();
        if (context == null) {
            return;
        }

        String command = String.format("adb shell sh %s", FreezeUtil.getShellFilePath(context));
        new AlertDialog.Builder(getContext())
                .setMessage("$ " + command)
                .setPositiveButton(R.string.btn_copy, (dialog, which) -> {
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(command);
                })
                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> {

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
