package com.john.freezeapp.usagestats;

import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;
import com.john.freezeapp.usagestats.appstandby.AppStandbyBucketViewHolder;
import com.john.freezeapp.usagestats.appstandby.AppStandbyData;

import java.util.List;

public class UsageStatsAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public UsageStatsAdapter() {
        getCreatorPool().putRule(UsageStatsData.class, UsageStatsViewHolder.CREATOR);
        getCreatorPool().putRule(AppStandbyData.class, AppStandbyBucketViewHolder.CREATOR);
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
