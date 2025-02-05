package com.john.freezeapp.storage;

import android.content.pm.PackageInfo;

import com.john.freezeapp.util.FreezeAppManager;

import java.util.List;

public class Storage {


    public void request() {
        List<PackageInfo> installApp = FreezeAppManager.getInstallApp(FreezeAppManager.TYPE_NORMAL_APP, FreezeAppManager.STATUS_ALL, true);
    }
}
