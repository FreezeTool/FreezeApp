package com.john.freezeapp.main.tool.data;

import android.view.View;

public class FreezeHomeToolItemData extends FreezeHomeToolBaseData  implements ISpanSizeLookup {

    public FreezeHomeToolItemData(String text, int icon, int bgColor, View.OnClickListener clickListener) {
        super(text, icon, bgColor, clickListener);
    }

    public static FreezeHomeToolItemData transform(FreezeHomeToolModel model) {
        FreezeHomeToolItemData data = new FreezeHomeToolItemData(model.shortTitle, model.icon, model.bgColor, model.clickListener);
        return data;
    }

    @Override
    public int getSpanSize() {
        return TOOL_ITEM;
    }
}
