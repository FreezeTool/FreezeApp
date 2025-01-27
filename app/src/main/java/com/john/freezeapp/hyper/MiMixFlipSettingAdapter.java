package com.john.freezeapp.hyper;

import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.ClassCreatorPool;

import java.util.List;

public class MiMixFlipSettingAdapter extends CardRecyclerViewAdapter<ClassCreatorPool> {

    public MiMixFlipSettingAdapter(OnItemClickListener onItemClickListener) {
        getCreatorPool().putRule(MiMixFlipAppData.class, MiMixFlipAppViewHolder.CREATOR);
        setHasStableIds(true);
        setListener(onItemClickListener);
    }

    public interface OnItemClickListener {
        void forceStop(CardData data);

        void scaleSetting(CardData data);
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
        setItems(list);
        notifyDataSetChanged();
    }

    public void addData(Object o) {
        getItems().add(o);
        notifyDataSetChanged();
    }
}
