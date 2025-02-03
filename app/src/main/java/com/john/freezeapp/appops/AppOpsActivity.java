package com.john.freezeapp.appops;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.usagestats.UsageStats;
import com.john.freezeapp.usagestats.UsageStatsData;
import com.john.freezeapp.util.FreezeAppManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppOpsActivity extends BaseActivity {

    AppOpsAdapter mAdapter = new AppOpsAdapter(null);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_ops);

        if (!isDaemonActive()) {
            finish();
            return;
        }

        initToolbar();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        requestInstallApp();
    }

    private void requestInstallApp() {
        showLoading();
        FreezeAppManager.requestAppList(this, FreezeAppManager.TYPE_NORMAL_APP, FreezeAppManager.STATUS_ALL, false, new FreezeAppManager.Callback() {
            @Override
            public void success(List<FreezeAppManager.AppModel> list) {
                hideLoading();
                requestUsageApp(list);
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });

    }


    private void requestUsageApp(List<FreezeAppManager.AppModel> list) {
        showLoading();
        UsageStats.requestUsageStatsData(this, new UsageStats.Callback() {
            @Override
            public void success(List<UsageStatsData> usageStatsDataList) {
                usageStatsDataList.removeIf(next -> next.totalTimeVisible <= 0);
                Collections.sort(usageStatsDataList);
                updateData(list, usageStatsDataList);
                hideLoading();
            }

            @Override
            public void fail() {
                updateData(list, null);
                hideLoading();
            }
        });
    }

    private void updateData(List<FreezeAppManager.AppModel> userAppList, List<UsageStatsData> usageStatsDataList) {
        List<AppOpsData> data = new ArrayList<>();
        // usageStats 记录
        if (usageStatsDataList != null) {
            for (UsageStatsData usageStatsData : usageStatsDataList) {
                FreezeAppManager.AppModel app = userAppList.stream().filter(appModel -> TextUtils.equals(appModel.packageName, usageStatsData.packageName)).findFirst().orElse(null);
                if (app != null) {
                    userAppList.remove(app);
                    AppOpsData appOpsData = new AppOpsData(app);
                    data.add(appOpsData);
                }
            }
        }


        for (FreezeAppManager.AppModel appModel : userAppList) {
            data.add(new AppOpsData(appModel));
        }

        if (isDestroy()) {
            return;
        }

        postUI(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateData(data);
            }
        });
    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
