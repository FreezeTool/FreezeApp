package com.john.freezeapp.battery;

import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class BatteryUsageAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {

    public BatteryUsageAdapter() {
        getCreatorPool().putRule(BatteryUsageDeviceData.class, BatteryUsageDeviceViewHolder.CREATOR);
        getCreatorPool().putRule(BatteryUsageAppData.class, BatteryUsageAppViewHolder.CREATOR);
        getCreatorPool().putRule(BatteryUsageSummaryData.class, BatteryUsageSummaryViewHolder.CREATOR);
        getCreatorPool().putRule(BatteryUsageTitleData.class, BatteryUsageTitleViewHolder.CREATOR);
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
