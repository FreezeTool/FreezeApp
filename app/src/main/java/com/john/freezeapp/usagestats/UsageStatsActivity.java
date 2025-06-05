package com.john.freezeapp.usagestats;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarSearchActivity;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.ThreadPool;
import com.john.freezeapp.daemon.util.UIExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsageStatsActivity extends ToolbarSearchActivity {

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
        requestUsageStats();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.usage_stats_name);
    }

    private void updateData() {
        if (isDestroy()) {
            return;
        }
        if (mUsageStatsList != null) {
            String query = getQuery();
            if (TextUtils.isEmpty(query)) {
                UIExecutor.postUI(() -> mAdapter.updateData(mUsageStatsList));
            } else {
                List<UsageStatsData> list = new ArrayList<>(mUsageStatsList);
                ThreadPool.execute(() -> {
                    List<UsageStatsData> queryLists = new ArrayList<>();
                    for (UsageStatsData usageStatsData : list) {
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

    List<UsageStatsData> mUsageStatsList;

    private void requestUsageStats() {
        showLoading();
        UsageStats.requestUsageStatsData(this, new UsageStats.Callback() {
            @Override
            public void success(List<UsageStatsData> usageStatsDataList) {
                hideLoading();
                if (isDestroy()) {
                    return;
                }
                usageStatsDataList.removeIf(next -> next.totalTimeVisible <= 0);
                Collections.sort(usageStatsDataList);
                mUsageStatsList = usageStatsDataList;
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
