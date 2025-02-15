package com.john.freezeapp.clipboard;

import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class ClipboardAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public ClipboardAdapter() {
        getCreatorPool().putRule(ClipboardCardData.class, ClipboardViewHolder.CREATOR);
        setHasStableIds(true);
    }

    public interface OnItemClick {
        void onItemClick(CardData cardData);
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
