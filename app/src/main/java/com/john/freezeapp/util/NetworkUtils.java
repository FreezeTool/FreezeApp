package com.john.freezeapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public enum NetworkType {
        WIFI, MOBILE, NOT_CONNECTED
    }

    public static NetworkType getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    return NetworkType.WIFI; // 假定你有一个枚举 NetworkType 定义了网络类型
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return NetworkType.MOBILE; // 假定你有一个枚举 NetworkType 定义了网络类型
                }
            }
        }
        return NetworkType.NOT_CONNECTED; // 假定你有一个枚举 NetworkType 定义了网络类型
    }
}