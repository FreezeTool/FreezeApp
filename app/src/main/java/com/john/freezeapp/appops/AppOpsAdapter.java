package com.john.freezeapp.appops;

import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;
import com.john.freezeapp.util.FreezeAppManager;

import java.util.List;

public class AppOpsAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public AppOpsAdapter(OnItemClick onItemClick) {
        getCreatorPool().putRule(AppOpsData.class, AppOpsViewHolder.CREATOR);
        getCreatorPool().putRule(AppOpsDetailData.class, AppOpsDetailViewHolder.CREATOR);
        getCreatorPool().putRule(AppOpsPackageDetailData.class, AppOpsPackageDetailViewHolder.CREATOR);
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
        void refreshAppOps(String packageName);
    }
}
