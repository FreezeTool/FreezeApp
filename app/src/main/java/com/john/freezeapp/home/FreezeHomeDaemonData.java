package com.john.freezeapp.home;

import android.view.View;

public class FreezeHomeDaemonData extends FreezeHomeData {
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

