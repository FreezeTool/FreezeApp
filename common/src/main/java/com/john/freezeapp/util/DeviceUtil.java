package com.john.freezeapp.util;

import android.os.Build;

/**
 * Android 16       36
 * Android 15       35
 * Android 14	    34
 * Android 13	    33
 * Android 12L	    32
 * Android 12	    31
 * Android 11	    30
 * Android 10	    29
 * Android 9	    28
 * Android 8.1	    27
 * Android 8.0	    26
 * Android 7.1.1	25
 * Android 7.0	    24
 * Android 6.0	    23
 * Android 5.1	    22
 */
public class DeviceUtil {

    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static boolean atLeast26() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean atLeast24() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean atLeast28() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean atLeast27() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }

    public static boolean atLeast29() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static boolean atLeast30() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    public static boolean atLeast31() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    public static boolean atLeast33() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    public static boolean atLeast34() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE;
    }

    public static boolean atLeast35() {
        return Build.VERSION.SDK_INT >= 35;
    }

    public static boolean atLeast36() {
        return Build.VERSION.SDK_INT >= 36;
    }
}
