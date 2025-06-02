package com.john.freezeapp.traffic;

import android.net.NetworkTemplate;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.SwitchCompat;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.util.CommonUtil;

public class TrafficMonitorActivity extends ToolbarActivity {

    private TextView mTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_monitor);
        mTip = findViewById(R.id.tip);
        updateTip(ClientTrafficMonitor.isActive());
        initRadioGroup();
        initSwitcher();
        initSeekbar();
    }

    private void initRadioGroup() {
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        int matchRule = ClientTrafficMonitor.getTrafficMatchRule();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            ClientTrafficMonitor.setTrafficMatchRule(getMatchRule(checkedId));
            if (ClientTrafficMonitor.isActive()) {
                startTrafficMonitor();
            }
        });
        radioGroup.check(getCheckId(matchRule));

    }

    private int getMatchRule(int checkedId) {
        if (R.id.radio1 == checkedId) {
            return NetworkTemplate.MATCH_MOBILE;
        }
        return NetworkTemplate.MATCH_WIFI;
    }

    private int getCheckId(int matchRule) {
        if (matchRule == NetworkTemplate.MATCH_MOBILE) {
            return R.id.radio1;
        }
        return R.id.radio2;
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
                    startTrafficMonitor();
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
        return String.format("流量阈值：%s", CommonUtil.getSizeText(textSize * 1024 * 1024));
    }

    private void initSwitcher() {
        SwitchCompat switchCompat = findViewById(R.id.switcher);
        switchCompat.setChecked(ClientTrafficMonitor.isActive());
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    startTrafficMonitor();
                } else {
                    stopTrafficMonitor();
                }
            }
        });
    }

    private void stopTrafficMonitor() {
        ClientTrafficMonitor.stop();
        updateTip(false);
    }

    private void updateTip(boolean isOpen) {
        mTip.setText(isOpen ? "流量监控通知，可能会延迟1-3秒～" : "开启后，超过流量阈值将收到通知提醒～");
    }

    private void startTrafficMonitor() {
        ClientTrafficMonitor.start(getThresholdValue(ClientTrafficMonitor.getTrafficThreshold()), ClientTrafficMonitor.getTrafficMatchRule());
        updateTip(true);
    }
}
