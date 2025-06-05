package com.john.freezeapp.deviceidle;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarSearchActivity;
import com.john.freezeapp.common.CommonAdapter;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.PackageUtil;
import com.john.freezeapp.util.ThreadPool;
import com.john.freezeapp.daemon.util.UIExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeviceIdleActivity extends ToolbarSearchActivity {

    RecyclerView recyclerView;
    CommonAdapter mCommonAdapter = new CommonAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_idle);

        if (!isDaemonActive()) {
            finish();
            return;
        }
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mCommonAdapter);

        requestUserPowerWhitelist();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.app_device_idle);
    }

    private void updateData() {
        if (isDestroy()) {
            return;
        }
        if (mDeviceIdleList != null) {
            String query = getQuery();
            if (TextUtils.isEmpty(query)) {
                UIExecutor.postUI(() -> mCommonAdapter.updateData(mDeviceIdleList));
            } else {
                List<DeviceIdleData> list = new ArrayList<>(mDeviceIdleList);
                ThreadPool.execute(() -> {
                    List<DeviceIdleData> queryLists = new ArrayList<>();
                    for (DeviceIdleData usageStatsData : list) {
                        FreezeAppManager.CacheAppModel appModel = FreezeAppManager.getAppModel(getContext(), usageStatsData.packageName);
                        if (!TextUtils.isEmpty(appModel.name) && appModel.name.toLowerCase().contains(query.toLowerCase())) {
                            queryLists.add(usageStatsData);
                        }
                    }
                    UIExecutor.postUI(() -> mCommonAdapter.updateData(queryLists));
                });
            }
        }
    }

    List<DeviceIdleData> mDeviceIdleList;

    private void requestUserPowerWhitelist() {
        showLoading();
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<DeviceIdleData> userPowerWhitelist = DeviceIdle.getUserPowerWhitelist();
                List<PackageInfo> installApp = FreezeAppManager.getInstallApp(PackageUtil.TYPE_NORMAL_APP, PackageUtil.STATUS_ALL, true);
                List<DeviceIdleData> deviceIdleList = new ArrayList<>();

                for (PackageInfo packageInfo : installApp) {
                    DeviceIdleData data = userPowerWhitelist.stream().filter(deviceIdleData -> TextUtils.equals(packageInfo.packageName, deviceIdleData.packageName)).findFirst().orElse(null);
                    if (data != null) {
                        data.whiteList = 1;
                        deviceIdleList.add(data);
                    } else {
                        DeviceIdleData deviceIdleData = new DeviceIdleData(packageInfo.packageName);
                        deviceIdleData.whiteList = 0;
                        deviceIdleList.add(deviceIdleData);
                    }
                }
                Collections.sort(deviceIdleList);
                mDeviceIdleList = deviceIdleList;
                hideLoading();
                updateData();
            }
        });
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        finish();
    }

    @Override
    protected void onQueryTextChange(String query) {
        super.onQueryTextChange(query);
        updateData();
    }

    @Override
    protected void onQueryTextClose() {
        super.onQueryTextClose();
        updateData();
    }
}
