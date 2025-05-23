package com.john.freezeapp.deviceidle;

import com.john.freezeapp.client.ClientSystemService;

import java.util.ArrayList;
import java.util.List;

public class DeviceIdle {
    public static List<DeviceIdleData> getUserPowerWhitelist() {
        String[] userPowerWhitelist = ClientSystemService.getDeviceIdleController().getUserPowerWhitelist();

        if (userPowerWhitelist != null) {
            List<DeviceIdleData> deviceIdleList = new ArrayList<>();
            for (String s : userPowerWhitelist) {
                deviceIdleList.add(new DeviceIdleData(s));
            }
            return deviceIdleList;
        }

        return null;
    }

    public static boolean isPowerWhitelist(String packageName) {
        return ClientSystemService.getDeviceIdleController().isPowerSaveWhitelistApp(packageName);
    }


    public static void removePowerSaveWhitelistApp(String packageName) {
        ClientSystemService.getDeviceIdleController().removePowerSaveWhitelistApp(packageName);
    }


    public static void addPowerSaveWhitelistApp(String packageName) {
        ClientSystemService.getDeviceIdleController().addPowerSaveWhitelistApp(packageName);
    }
}
