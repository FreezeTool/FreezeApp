package com.john.freezeapp.util;

import androidx.appcompat.app.AppCompatDelegate;

public class SettingUtil {
    public static void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        SharedPrefUtil.setInt(SharedPrefUtil.KEY_SETTING_NIGHT_MODE, nightMode);
    }

    public static @AppCompatDelegate.NightMode int getNightMode() {
        return SharedPrefUtil.getInt(SharedPrefUtil.KEY_SETTING_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
