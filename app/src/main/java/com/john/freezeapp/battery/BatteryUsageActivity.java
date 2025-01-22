package com.john.freezeapp.battery;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;

import java.util.List;


@RequiresApi(Build.VERSION_CODES.S)
public class BatteryUsageActivity extends BaseActivity {

    BatteryUsageAdapter mAdapter = new BatteryUsageAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_usage);

        if (!isDaemonActive()) {
            finish();
            return;
        }
        initToolbar();


        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        requestBatteryUsage();
    }

    private void requestBatteryUsage() {
        showLoading();
        BatteryStats.requestBatteryUsage(this, new BatteryStats.Callback() {
            @Override
            public void success(List list) {
                hideLoading();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.updateData(list);
                    }
                });
            }

            @Override
            public void fail() {

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
