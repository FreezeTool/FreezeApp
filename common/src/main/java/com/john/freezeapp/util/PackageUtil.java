package com.john.freezeapp.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class PackageUtil {
    public static String FREEZE_APP_PACKAGE = "com.john.freezeapp";

    public static List<PackageInfo> filterApp(List<PackageInfo> packageInfos, @TYPE int appType, @STATUS int appStatus, boolean ignoreFreezeApp) {
        List<PackageInfo> filterPackageInfos = new ArrayList<>();
        for (PackageInfo packageInfo : packageInfos) {
            if (TextUtils.equals(packageInfo.packageName, FREEZE_APP_PACKAGE) && ignoreFreezeApp) {
                continue;
            }

            boolean isApex = false;
            if (DeviceUtil.atLeast29()) {
                isApex = packageInfo.isApex;
            }
            final boolean isSystem = !isApex && (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            final boolean isEnabled = !isApex && packageInfo.applicationInfo.enabled;

            if (appType != TYPE_ALL) {
                if ((appType == TYPE_SYSTEM_APP && !isSystem) || (appType == TYPE_NORMAL_APP && isSystem)) {
                    continue;
                }
            }

            if (appStatus != STATUS_ALL) {
                if ((appStatus == STATUS_DISABLE_APP && isEnabled) || (appStatus == STATUS_ENABLE_APP && !isEnabled)) {
                    continue;
                }
            }


            filterPackageInfos.add(packageInfo);
        }

        return filterPackageInfos;
    }

    @IntDef(value = {
            TYPE_ALL,
            TYPE_SYSTEM_APP,
            TYPE_NORMAL_APP
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    @IntDef(value = {
            STATUS_ALL,
            STATUS_ENABLE_APP,
            STATUS_DISABLE_APP,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATUS {
    }

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SYSTEM_APP = 1;
    public static final int TYPE_NORMAL_APP = 2;

    public static final int STATUS_ALL = 0;
    public static final int STATUS_ENABLE_APP = 1;
    public static final int STATUS_DISABLE_APP = 2;

}
