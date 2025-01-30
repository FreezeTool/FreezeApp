package com.john.freezeapp.monitor;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.util.FreezeUtil;

public class AppMonitorActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_monitor);
        initToolbar();
        SwitchCompat switchCompat = findViewById(R.id.switcher);
        switchCompat.setChecked(AppMonitorManager.isAppMonitor() && FreezeUtil.isOverlayPermission(AppMonitorActivity.this));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {

                    if (!FreezeUtil.isOverlayPermission(AppMonitorActivity.this)) {
                        buttonView.setChecked(false);
                        FreezeUtil.toOverlayPermissionPage(AppMonitorActivity.this);
                        return;
                    }

                    AppMonitorManager.setAppMonitor(isChecked);
                    if (isChecked) {
                        AppMonitorManager.startAppMonitor(AppMonitorActivity.this);
                    } else {
                        AppMonitorManager.stopAppMonitor(AppMonitorActivity.this);
                    }
                }
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
