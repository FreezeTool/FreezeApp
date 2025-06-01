package com.john.freezeapp.main.home;

import android.view.View;

import com.john.freezeapp.main.FreezeMainData;

public class FreezeHomeDaemonData extends FreezeMainData {
    public String title;
    public String subTitle;
    public String content;
    public int icon;

    public DaemonBtnData rightDaemonBtnData;
    public DaemonBtnData leftDaemonBtnData;



    public static class DaemonBtnData {
        public View.OnClickListener onClickListener;
        public String text;
        public int icon;
        public boolean show;
    }


}

