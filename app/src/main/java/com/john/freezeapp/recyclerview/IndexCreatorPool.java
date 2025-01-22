package com.john.freezeapp.recyclerview;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class IndexCreatorPool implements CreatorPool {

    private final List<CardViewHolder.Creator> mCreators;
    private final List<Integer> mPositionToIndex;

    public IndexCreatorPool() {
        mCreators = new ArrayList<>();
        mPositionToIndex = new ArrayList<>();
    }

    public void add(CardViewHolder.Creator creator) {
        int indexOfCreator = mCreators.indexOf(creator);
        if (indexOfCreator == -1) {
            mCreators.add(creator);
            indexOfCreator = mCreators.size() - 1;
        }
        mPositionToIndex.add(indexOfCreator);
    }

    public void add(int itemPosition, CardViewHolder.Creator creator) {
        int indexOfCreator = mCreators.indexOf(creator);
        if (indexOfCreator == -1) {
            mCreators.add(creator);
            indexOfCreator = mCreators.size() - 1;
        }
        mPositionToIndex.add(itemPosition, indexOfCreator);
    }

    public void remove(int itemPosition) {
        mPositionToIndex.remove(itemPosition);
    }

    public void clear() {
        mPositionToIndex.clear();
    }

    @Override
    public int getCreatorIndex(CardRecyclerViewAdapter adapter, int position) {
        return mPositionToIndex.get(position);
    }

    @Override
    public CardViewHolder.Creator getCreator(int index) {
        return mCreators.get(index);
    }
}
