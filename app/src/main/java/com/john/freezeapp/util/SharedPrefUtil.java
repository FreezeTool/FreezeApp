package com.john.freezeapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.john.freezeapp.App;

public class SharedPrefUtil {

    private static final String NAMESPACE = "FreezeApp";
    public static final String KEY_KERNEL_VERSION = "KEY_KERNEL_VERSION";

    public static SharedPreferences getSharedPref() {
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
