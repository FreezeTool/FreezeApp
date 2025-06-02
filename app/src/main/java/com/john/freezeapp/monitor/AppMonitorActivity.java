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
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.traffic.ClientTrafficMonitor;
import com.john.freezeapp.util.FreezeUtil;

public class AppMonitorActivity extends ToolbarActivity {

    private static final int MIN_SEEKBAR = 5;
    private static final int MAX_SEEKBAR = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_monitor);
        initSwitcher();
        initSeekbar();
    }

    @Override
    protected String getToolbarTitle() {
        return getContext().getString(R.string.app_monitor);
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
        switchCompat.setChecked(ClientTrafficMonitor.isActive());
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
}
