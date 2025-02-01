package com.john.freezeapp.appops;

import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.util.FreezeAppManager;

public class AppOpsDetailData extends CardData implements Comparable<AppOpsDetailData> {
    public int mode;
    public int defaultMode;
    public int op;
    public int opSwitchCode;
    public AppOps.AppOpsDetail appOpInfo;
    public int sort;
    public String packageName;
    public long lastAccessTime;
    public long lastRejectTime;
    public boolean isRunning;
    public long nowTime = System.currentTimeMillis();

    public AppOpsDetailData(AppOps.AppOpsDetail appOpInfo) {
        this.appOpInfo = appOpInfo;
        this.sort = getSort(appOpInfo.mode);
        this.op = appOpInfo.code;
        this.mode = appOpInfo.mode;
        this.defaultMode = appOpInfo.defaultMode;
        this.packageName = appOpInfo.packageName;
        this.lastAccessTime = appOpInfo.lastAccessTime;
        this.lastRejectTime = appOpInfo.lastRejectTime;
        this.isRunning = appOpInfo.isRunning;
        this.opSwitchCode = appOpInfo.opSwitchCode;

    }

    private int getSort(int mode) {
        switch (mode) {
            case AppOps.MODE_ALLOWED:
                return 0;
            case AppOps.MODE_IGNORED:
                return 3;
            case AppOps.MODE_ERRORED:
                return 5;
            case AppOps.MODE_DEFAULT:
                return 5;
            case AppOps.MODE_FOREGROUND:
                return 1;
            case AppOps.MODE_ASK:
                return 2;
            default:
                return 6;
        }
    }


    @Override
    public int compareTo(AppOpsDetailData o) {
        return sort - o.sort;
    }
}
