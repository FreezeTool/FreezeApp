package com.john.freezeapp.adb;

import android.view.View;

import com.john.freezeapp.recyclerview.CardData;

public class AdbPairData extends CardData {
    public String subTitle;
    public int icon;
    public AdbPairBtnData rightBtnData;



    public static class AdbPairBtnData {
        public View.OnClickListener onClickListener;
        public String text;
        public int icon;
        public boolean show;
    }
}
