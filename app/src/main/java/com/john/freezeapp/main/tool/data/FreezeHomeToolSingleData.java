package com.john.freezeapp.main.tool.data;

import android.view.View;


public class FreezeHomeToolSingleData extends FreezeHomeToolBaseData {


    public FreezeHomeToolSingleData(String text, int icon, int bgColor, View.OnClickListener clickListener) {
        super(text, icon, bgColor, clickListener);
    }

    public static FreezeHomeToolSingleData transform(FreezeHomeToolModel model) {
        FreezeHomeToolSingleData data = new FreezeHomeToolSingleData(model.title, model.icon, model.bgColor, model.clickListener);
        return data;
    }

    @Override
    public int getSpanSize() {
        return TOOL_SINGLE;
    }
}
