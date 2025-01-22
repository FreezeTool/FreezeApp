package com.john.freezeapp.freeze;

import com.john.freezeapp.FreezeAppManager;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class FreezeAppAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {

    public FreezeAppAdapter(OnItemClick onItemClick) {
        getCreatorPool().putRule(FreezeAppData.class, FreezeAppViewHolder.CREATOR);
        setHasStableIds(true);
        setListener(onItemClick);
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

    public interface OnItemClick {
        void onRightClick(FreezeAppManager.AppModel appModel);
    }
}
