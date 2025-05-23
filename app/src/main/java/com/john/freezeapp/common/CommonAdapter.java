package com.john.freezeapp.common;

import com.john.freezeapp.deviceidle.DeviceIdleData;
import com.john.freezeapp.deviceidle.DeviceIdleViewHolder;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;
import com.john.freezeapp.usagestats.appstandby.AppStandbyBucketViewHolder;
import com.john.freezeapp.usagestats.appstandby.AppStandbyData;

import java.util.List;

public class CommonAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public CommonAdapter() {
        getCreatorPool().putRule(DeviceIdleData.class, DeviceIdleViewHolder.CREATOR);
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