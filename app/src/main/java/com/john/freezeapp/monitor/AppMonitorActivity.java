package com.john.freezeapp.monitor;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.util.FreezeUtil;

public class AppMonitorActivity extends BaseActivity {

    private static final int MIN_SEEKBAR = 5;
    private static final int MAX_SEEKBAR = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_monitor);
        initToolbar();
        initSwitcher();
        initSeekbar();
    }

    private void initSeekbar() {
        AppCompatSeekBar seekBar = findViewById(R.id.seekbar);
        TextView tvSeekbarValue = findViewById(R.id.tv_seekbar_value);
        int textSize = AppMonitorManager.getTextSize();
        tvSeekbarValue.setText(getSeekbarText(textSize));
        int range = MAX_SEEKBAR - MIN_SEEKBAR;
        int process = (int) ((float) (textSize - MIN_SEEKBAR) * 100 / range);
        seekBar.setProgress(process);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (int) ((float) progress / 100 * range) + MIN_SEEKBAR;
                AppMonitorManager.setTextSize(value);
                tvSeekbarValue.setText(getSeekbarText(value));
                AppMonitorManager.updateAppMonitorTextSize(AppMonitorActivity.this);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String getSeekbarText(int textSize) {
        return "文字大小：" + textSize;
    }

    private void initSwitcher() {
        SwitchCompat switchCompat = findViewById(R.id.switcher);
        switchCompat.setChecked(AppMonitorManager.isAppMonitor());
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        AppMonitorManager.setAppMonitor(isChecked);
                        AppMonitorManager.startAppMonitor(AppMonitorActivity.this);
                    } else {
                        AppMonitorManager.setAppMonitor(isChecked);
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
