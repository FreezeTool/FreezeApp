package com.john.freezeapp.appops;

import com.john.freezeapp.recyclerview.CardData;

public class AppOpsDetailData extends CardData {
    public int op;
    public int switchOp;
    public int uidMode;
    public int pkgMode;
    public int defMode;
    public int uid;
    public String packageName;
    public long lastAccessTime;
    public long lastRejectTime;
    public boolean isRunning;
    public long nowTime = System.currentTimeMillis();
    public boolean ignoreSetting = false;

    public AppOps.AppOpsDetail appOpInfo;

    public AppOpsDetailData(AppOps.AppOpsDetail appOpInfo) {
        this.appOpInfo = appOpInfo;
        this.op = appOpInfo.op;
        this.uidMode = appOpInfo.uidMode;
        this.defMode = appOpInfo.defMode;
        this.pkgMode = appOpInfo.pkgMode;
        this.switchOp = appOpInfo.switchOp;
        this.uid = appOpInfo.uid;
        this.packageName = appOpInfo.packageName;
        this.lastAccessTime = appOpInfo.lastAccessTime;
        this.lastRejectTime = appOpInfo.lastRejectTime;
        this.isRunning = appOpInfo.isRunning;


    }
}
