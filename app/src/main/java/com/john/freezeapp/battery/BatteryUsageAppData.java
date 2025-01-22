package com.john.freezeapp.battery;

import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class BatteryUsageAppData {

    public List<HardwareData> hardwareDatas = new ArrayList<>();
    public int uid;
    public String packageName;
    public long foregroundTime;
    public long backgroundTime;
    public Drawable icon;
    public String name;
    public LinearLayout cacheView;
    public boolean isExpand = false;

    public void addHardwareData(HardwareData data) {
        hardwareDatas.add(data);
    }

    public boolean isEmpty() {
        return hardwareDatas.isEmpty();
    }


    public static class HardwareData {
        public String label;
        public String content;
    }
}
