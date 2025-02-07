package com.john.freezeapp.storage;

import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class StorageAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public StorageAdapter(OnItemClick onItemClick) {
        getCreatorPool().putRule(StorageData.class, StorageViewHolder.CREATOR);
        setHasStableIds(true);
        setListener(onItemClick);
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
