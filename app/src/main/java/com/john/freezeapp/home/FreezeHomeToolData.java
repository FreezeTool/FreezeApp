package com.john.freezeapp.home;

import android.view.View;

public class FreezeHomeToolData extends FreezeHomeData {
    public String text;
    public View.OnClickListener clickListener;
    public int icon;
    public int bgColor;

    public FreezeHomeToolData(String text, int icon, int bgColor, View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.bgColor = bgColor;
        this.icon = icon;
        this.text = text;
    }
}
