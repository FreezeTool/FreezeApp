package com.john.freezeapp.main.tool.data;

import android.view.View;

public class FreezeHomeToolBaseData extends FreezeHomeToolData {
    public String text;
    public View.OnClickListener clickListener;
    public int icon;
    public int bgColor;

    public FreezeHomeToolBaseData(String text, int icon, int bgColor, View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.bgColor = bgColor;
        this.icon = icon;
        this.text = text;
    }
}
