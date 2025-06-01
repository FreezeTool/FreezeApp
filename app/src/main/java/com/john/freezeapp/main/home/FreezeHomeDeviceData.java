package com.john.freezeapp.main.home;

import android.view.ViewGroup;

import com.john.freezeapp.main.FreezeMainData;

import java.util.ArrayList;
import java.util.List;

public class FreezeHomeDeviceData extends FreezeMainData {

    public List<FreezeHomeDeviceInfoData> deviceInfos = new ArrayList<>();
    public ViewGroup cacheView;

    public FreezeHomeDeviceData() {

    }


    public void add(FreezeHomeDeviceInfoData deviceInfo) {
        deviceInfos.add(deviceInfo);
    }

}
