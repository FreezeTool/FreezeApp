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
import com.john.freezeapp.ToolbarActivity;

import java.util.List;


@RequiresApi(Build.VERSION_CODES.S)
public class BatteryUsageActivity extends ToolbarActivity {

    BatteryUsageAdapter mAdapter = new BatteryUsageAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_usage);

        if (!isDaemonActive()) {
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        requestBatteryUsage();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.battery_usage_name);
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
}
