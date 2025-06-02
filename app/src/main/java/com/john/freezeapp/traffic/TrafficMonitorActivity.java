package com.john.freezeapp.traffic;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.SwitchCompat;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.monitor.AppMonitorManager;
import com.john.freezeapp.util.CommonUtil;
import com.john.freezeapp.util.FreezeUtil;

public class TrafficMonitorActivity extends ToolbarActivity {

    public static final int[] GAP = new int[]{0, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_monitor);
        initSwitcher();
        initSeekbar();
    }

    @Override
    protected String getToolbarTitle() {
        return getContext().getString(R.string.app_traffic_monitor);
    }

    private void initSeekbar() {
        AppCompatSeekBar seekBar = findViewById(R.id.seekbar);
        TextView tvSeekbarValue = findViewById(R.id.tv_seekbar_value);
        seekBar.setMax(100);
        int threshold = ClientTrafficMonitor.getTrafficThreshold();
        seekBar.setProgress(threshold);
        tvSeekbarValue.setText(getSeekbarText(getDisplayValue(threshold)));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekbarValue.setText(getSeekbarText(getDisplayValue(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                ClientTrafficMonitor.setTrafficThreshold(progress);
                if (ClientTrafficMonitor.isActive()) {
                    ClientTrafficMonitor.start(getThresholdValue(progress));
                }
            }
        });
    }

    public int getDisplayValue(int process) {
        return (int) Math.pow(2, (float) process / 10);
    }

    public int getThresholdValue(int process) {
        return getDisplayValue(process) * 1024 * 1024;
    }

    private String getSeekbarText(int textSize) {
        return String.format("移动数据流量阈值：%s", CommonUtil.getSizeText(textSize * 1024 * 1024));
    }

    private void initSwitcher() {
        SwitchCompat switchCompat = findViewById(R.id.switcher);
        switchCompat.setChecked(ClientTrafficMonitor.isActive());
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    ClientTrafficMonitor.start(getThresholdValue(ClientTrafficMonitor.getTrafficThreshold()));
                } else {
                    ClientTrafficMonitor.stop();
                }
            }
        });
    }
}
