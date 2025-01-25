package com.john.freezeapp.home;

import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class FreezeHomeAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public FreezeHomeAdapter() {
        getCreatorPool().putRule(FreezeHomeFuncData.class, FreezeHomeFuncViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeBillboardData.class, FreezeHomeBillboardViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeDeviceData.class, FreezeHomeDeviceViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeDaemonData.class, FreezeHomeDaemonViewHolder.CREATOR);
        getCreatorPool().putRule(CommonEmptyData.class, CommonEmptyViewHolder.CREATOR);
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
