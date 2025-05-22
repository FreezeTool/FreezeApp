package com.john.freezeapp.usagestats.appstandby;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarSearchActivity;
import com.john.freezeapp.usagestats.UsageStatsAdapter;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.ThreadPool;
import com.john.freezeapp.util.UIExecutor;

import java.util.ArrayList;
import java.util.List;

public class AppStandbyActivity extends ToolbarSearchActivity {

    RecyclerView recyclerView;
    UsageStatsAdapter mAdapter = new UsageStatsAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_stats);

        if (!isDaemonActive()) {
            finish();
            return;
        }
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        requestAppStandbyBucket();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.app_standby_name);
    }

    private void updateData() {
        if (isDestroy()) {
            return;
        }
        if (mAppStandbyList != null) {
            String query = getQuery();
            if (TextUtils.isEmpty(query)) {
                UIExecutor.postUI(() -> mAdapter.updateData(mAppStandbyList));
            } else {
                List<AppStandbyData> list = new ArrayList<>(mAppStandbyList);
                ThreadPool.execute(() -> {
                    List<AppStandbyData> queryLists = new ArrayList<>();
                    for (AppStandbyData usageStatsData : list) {
                        FreezeAppManager.CacheAppModel appModel = FreezeAppManager.getAppModel(getContext(), usageStatsData.packageName);
                        if (!TextUtils.isEmpty(appModel.name) && appModel.name.toLowerCase().contains(query.toLowerCase())) {
                            queryLists.add(usageStatsData);
                        }
                    }
                    UIExecutor.postUI(() -> mAdapter.updateData(queryLists));
                });
            }
        }
    }

    List<AppStandbyData> mAppStandbyList;

    private void requestAppStandbyBucket() {
        showLoading();
        AppStandby.requestAppStandbyBucket(new AppStandby.Callback() {
            @Override
            public void success(List<AppStandbyData> appStandbyList) {
                hideLoading();
                if (isDestroy()) {
                    return;
                }
                List<PackageInfo> installApp = FreezeAppManager.getInstallApp(FreezeAppManager.TYPE_NORMAL_APP, FreezeAppManager.STATUS_ALL, true);
                List<AppStandbyData> installAppStandbyData = new ArrayList<>();
                for (AppStandbyData appStandbyData : appStandbyList) {
                    if(installApp.stream().anyMatch(packageInfo -> TextUtils.equals(packageInfo.packageName, appStandbyData.packageName))) {
                        installAppStandbyData.add(appStandbyData);
                    }
                }
                mAppStandbyList = installAppStandbyData;
                updateData();
            }

            @Override
            public void fail() {
                hideLoading();
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
