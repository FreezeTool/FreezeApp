package com.john.freezeapp.clipboard;

import com.john.freezeapp.home.CommonEmptyData;
import com.john.freezeapp.home.CommonEmptyViewHolder;
import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class ClipboardAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public ClipboardAdapter(OnItemClick onItemClick) {
        getCreatorPool().putRule(ClipboardCardData.class, ClipboardViewHolder.CREATOR);
        getCreatorPool().putRule(CommonEmptyData.class, CommonEmptyViewHolder.CREATOR);
        setHasStableIds(true);
        setListener(onItemClick);
    }


    public interface OnItemClick {
        void onItemClick(CardData cardData);
        void onLongItemClick(CardData cardData);
    }

    @Override
    public long getItemId(int position) {
        return getItemAt(position).hashCode();
    }

    @Override
    public ClassCreatorPool onCreateCreatorPool() {
        return new ClassCreatorPool();
    }

    public void updateData(List list) {
        getItems().clear();
        getItems().addAll(list);
        notifyDataSetChanged();
    }

    public void addData(CardData cardData) {
        getItems().add(cardData);
        notifyDataSetChanged();
    }
}
