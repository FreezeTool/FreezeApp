package com.john.freezeapp.recyclerview;

import android.view.View;

public class CardListenerViewHolder<T, L> extends CardViewHolder<T> {

    public CardListenerViewHolder(View itemView) {
        super(itemView);
    }

    public L getListener() {
        //noinspection unchecked
        return (L) getAdapter().getListener();
    }
}