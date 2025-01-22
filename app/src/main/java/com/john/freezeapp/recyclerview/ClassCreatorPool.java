package com.john.freezeapp.recyclerview;

import java.util.ArrayList;
import java.util.List;

public class ClassCreatorPool implements CreatorPool {

    private final List<Class<?>> mClasses;
    private final List<CardViewHolder.Creator> mCreators;

    public ClassCreatorPool() {
        mClasses = new ArrayList<>();
        mCreators = new ArrayList<>();
    }

    public <T> ClassCreatorPool putRule(Class<T> clazz, CardViewHolder.Creator<T> creator) {
        int position = mClasses.indexOf(clazz);
        if (position != -1) {
            mCreators.set(position, creator);
        } else {
            mClasses.add(clazz);
            mCreators.add(creator);
        }
        return this;
    }

    public int getCreatorIndex(CardRecyclerViewAdapter adapter, int position) {
        Object data = adapter.getItemAt(position);
        for (int i = 0; i < mClasses.size(); i++) {
            if (mClasses.get(i).isAssignableFrom(data.getClass())) {
                return i;
            }
        }
        return -1;
    }

    public CardViewHolder.Creator getCreator(int index) {
        return mCreators.get(index);
    }
}
