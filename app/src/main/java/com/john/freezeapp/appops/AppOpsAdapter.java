package com.john.freezeapp.appops;

import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class AppOpsAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public AppOpsAdapter() {
        getCreatorPool().putRule(AppOpsData.class, AppOpsViewHolder.CREATOR);
        getCreatorPool().putRule(AppOpsDetailData.class, AppOpsDetailViewHolder.CREATOR);
        setHasStableIds(true);
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
}
