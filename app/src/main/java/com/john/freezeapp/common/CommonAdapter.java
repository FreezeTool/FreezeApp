package com.john.freezeapp.common;

import com.john.freezeapp.deviceidle.DeviceIdleData;
import com.john.freezeapp.deviceidle.DeviceIdleViewHolder;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;
import com.john.freezeapp.runas.RunAsModel;
import com.john.freezeapp.runas.RunAsViewHolder;

import java.util.List;

public class CommonAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {

    public interface ItemListener {
        void onItemClick(Object object);
    }

    public CommonAdapter() {
        getCreatorPool().putRule(DeviceIdleData.class, DeviceIdleViewHolder.CREATOR);
        getCreatorPool().putRule(RunAsModel.class, RunAsViewHolder.CREATOR);
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