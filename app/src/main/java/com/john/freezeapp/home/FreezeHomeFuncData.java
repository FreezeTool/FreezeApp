package com.john.freezeapp.home;

import android.view.View;

public class FreezeHomeFuncData {
    public String text;
    public View.OnClickListener clickListener;

    public FreezeHomeFuncData(String text, View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.text = text;
    }
}
