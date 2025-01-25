package com.john.freezeapp.home;

import android.view.View;

public class FreezeHomeBillboardData extends FreezeHomeData{
    public boolean isActive;
    public View.OnClickListener onClickStartDaemon;


    public FreezeHomeBillboardData(boolean daemonActive, View.OnClickListener onClickStartDaemon) {
        this.isActive = daemonActive;
        this.onClickStartDaemon = onClickStartDaemon;
    }
}
