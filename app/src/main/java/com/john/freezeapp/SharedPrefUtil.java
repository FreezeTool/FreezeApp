package com.john.freezeapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    private static final String NAMESPACE = "FreezeApp";
    public static final String KEY_KERNEL_VERSION = "key_kernel_version";

    private static SharedPreferences getSharedPref() {
        return App.getApp().getSharedPreferences(NAMESPACE, Context.MODE_PRIVATE);
    }

    public static String getString(String key, String def) {
        return getSharedPref().getString(key, def);
    }

    public static void setString(String key, String def) {
        SharedPreferences.Editor edit = getSharedPref().edit();
        edit.putString(key, def);
        edit.apply();
    }
}
