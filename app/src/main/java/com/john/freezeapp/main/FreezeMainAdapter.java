package com.john.freezeapp.main;

import com.john.freezeapp.main.home.FreezeHomeBillboardData;
import com.john.freezeapp.main.home.FreezeHomeBillboardViewHolder;
import com.john.freezeapp.main.home.FreezeHomeDaemonData;
import com.john.freezeapp.main.home.FreezeHomeDaemonViewHolder;
import com.john.freezeapp.main.home.FreezeHomeDeviceData;
import com.john.freezeapp.main.home.FreezeHomeDeviceInfoData;
import com.john.freezeapp.main.home.FreezeHomeDeviceInfoViewHolder;
import com.john.freezeapp.main.home.FreezeHomeDeviceViewHolder;
import com.john.freezeapp.main.home.FreezeHomeLogData;
import com.john.freezeapp.main.home.FreezeHomeLogViewHolder;
import com.john.freezeapp.main.tool.FreezeHomeToolGroupViewHolder;
import com.john.freezeapp.main.tool.FreezeHomeToolItemViewHolder;
import com.john.freezeapp.main.tool.data.FreezeHomeToolGroupData;
import com.john.freezeapp.main.tool.data.FreezeHomeToolItemData;
import com.john.freezeapp.main.tool.data.FreezeHomeToolSingleData;
import com.john.freezeapp.main.tool.FreezeHomeToolSingleViewHolder;
import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class FreezeMainAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {
    public FreezeMainAdapter() {
        getCreatorPool().putRule(FreezeHomeToolSingleData.class, FreezeHomeToolSingleViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeBillboardData.class, FreezeHomeBillboardViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeDeviceData.class, FreezeHomeDeviceViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeDaemonData.class, FreezeHomeDaemonViewHolder.CREATOR);
        getCreatorPool().putRule(CommonEmptyData.class, CommonEmptyViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeLogData.class, FreezeHomeLogViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeDeviceInfoData.class, FreezeHomeDeviceInfoViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeToolGroupData.class, FreezeHomeToolGroupViewHolder.CREATOR);
        getCreatorPool().putRule(FreezeHomeToolItemData.class, FreezeHomeToolItemViewHolder.CREATOR);
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

    public void addData(Object o) {
        getItems().add(o);
        notifyDataSetChanged();
    }
}
