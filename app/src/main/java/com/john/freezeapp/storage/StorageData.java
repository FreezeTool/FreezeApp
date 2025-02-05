package com.john.freezeapp.storage;

import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.util.FreezeAppManager;

public class StorageData extends CardData {
    public FreezeAppManager.AppModel appModel;

    public StorageData(FreezeAppManager.AppModel appModel) {
        this.appModel = appModel;
    }
}
