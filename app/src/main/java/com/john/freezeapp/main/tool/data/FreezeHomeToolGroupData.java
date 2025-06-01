package com.john.freezeapp.main.tool.data;

public class FreezeHomeToolGroupData extends FreezeHomeToolData implements ISpanSizeLookup {
    public int group;
    public String text;

    public FreezeHomeToolGroupData(int group) {
        this.group = group;
    }

    @Override
    public int getSpanSize() {
        return TOOL_SINGLE;
    }

}
