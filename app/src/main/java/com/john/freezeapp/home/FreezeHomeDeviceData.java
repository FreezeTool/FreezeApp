package com.john.freezeapp.home;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FreezeHomeDeviceData extends FreezeHomeData {

    public List<FreezeHomeDeviceInfoData> deviceInfos = new ArrayList<>();
    public ViewGroup cacheView;

    public FreezeHomeDeviceData() {

    }


    public void add(FreezeHomeDeviceInfoData deviceInfo) {
        deviceInfos.add(deviceInfo);
    }

}
