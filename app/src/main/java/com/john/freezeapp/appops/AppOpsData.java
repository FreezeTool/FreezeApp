package com.john.freezeapp.appops;

import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.util.FreezeAppManager;

public class AppOpsData extends CardData {
    public FreezeAppManager.AppModel appModel;

    public AppOpsData(FreezeAppManager.AppModel appModel) {
        this.appModel = appModel;
    }
}
