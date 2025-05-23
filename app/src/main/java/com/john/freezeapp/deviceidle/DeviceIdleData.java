package com.john.freezeapp.deviceidle;

public class DeviceIdleData implements Comparable<DeviceIdleData> {


    public String packageName;
    public int whiteList = DEVICE_IDLE_NOT_WHITE_LIST;
    public static final int DEVICE_IDLE_NOT_WHITE_LIST = 0;
    public static final int DEVICE_IDLE_WHITE_LIST = 1;


    public DeviceIdleData(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public int compareTo(DeviceIdleData o) {
        return o.whiteList - whiteList;
    }
}
