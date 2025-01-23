package com.john.freezeapp.home;

import android.view.View;

public class FreezeHomeDaemonData extends FreezeHomeData{
    public boolean isActive;
    public View.OnClickListener onClickStartDaemon;


    public FreezeHomeDaemonData(boolean daemonActive, View.OnClickListener onClickStartDaemon) {
        this.isActive = daemonActive;
        this.onClickStartDaemon = onClickStartDaemon;
    }
}
