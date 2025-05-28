package com.john.freezeapp.daemon.util;

import android.content.SharedPreferences;

import com.john.freezeapp.daemon.DaemonHelper;

import java.io.File;

public class DaemonSharedPrefUtils {

    private static SharedPreferences sharedPreferences;

    static {
        File file = new File(DaemonHelper.DAEMON_CONFIG_PATH);
        sharedPreferences = new SharedPreferencesImpl(file);
    }


    public static String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public static void setString(String key, String def) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, def);
        edit.apply();
    }

    public static boolean getBoolean(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }


    public static int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }


}
