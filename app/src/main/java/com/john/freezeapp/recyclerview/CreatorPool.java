package com.john.freezeapp.recyclerview;

public interface CreatorPool {

    int getCreatorIndex(CardRecyclerViewAdapter adapter, int position);
    CardViewHolder.Creator getCreator(int index);
}
