package com.john.freezeapp.home;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FreezeHomeDeviceData {

    public List<DeviceInfo> deviceInfos = new ArrayList<>();
    public ViewGroup cacheView;

    public FreezeHomeDeviceData() {

    }


    public void add(DeviceInfo deviceInfo) {
        deviceInfos.add(deviceInfo);
    }


    public static class DeviceInfo {
        public String type;
        public String content;

        public DeviceInfo(String type, String content) {
            this.content = content;
            this.type = type;
        }
    }
}
