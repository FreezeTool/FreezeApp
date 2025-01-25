package com.john.freezeapp.home;

import com.john.freezeapp.recyclerview.CardData;

public class CommonEmptyData extends CardData {
    public String content;
    public int height;

    public CommonEmptyData(int height, String content) {
        this.height = height;
        this.content = content;
    }
}
