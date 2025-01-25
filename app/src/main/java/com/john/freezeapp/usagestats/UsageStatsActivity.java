package com.john.freezeapp.usagestats;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UsageStatsActivity extends BaseActivity {

    Toolbar toolbar;
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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        requestUsageStats();
    }

    private void updateData(List<UsageStatsData> usageStatsDataList) {
        usageStatsDataList.removeIf(next -> next.totalTimeVisible <= 0);
        Collections.sort(usageStatsDataList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateData(usageStatsDataList);
            }
        });
    }

    private void requestUsageStats() {
        showLoading();
        UsageStats.requestUsageStatsData(this, new UsageStats.Callback() {
            @Override
            public void success(List<UsageStatsData> usageStatsDataList) {
                hideLoading();
                if (isDestroy()) {
                    return;
                }
                updateData(usageStatsDataList);
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });
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
    protected void unbindDaemon() {
        super.unbindDaemon();
        finish();
    }
}
