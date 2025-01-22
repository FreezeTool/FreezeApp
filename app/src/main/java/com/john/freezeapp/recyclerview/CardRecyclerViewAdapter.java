package com.john.freezeapp.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class CardRecyclerViewAdapter<CP extends CreatorPool> extends RecyclerView.Adapter<CardViewHolder> {

    private static final String TAG = "BaseRVAdapter";
    private static final boolean DEBUG = false;

    private List mItems;
    private final CP mCreatorPool;
    private Object mListener;

    public Object getListener() {
        return mListener;
    }

    public void setListener(Object listener) {
        mListener = listener;
    }

    public CardRecyclerViewAdapter() {
        this(new ArrayList<>());
    }

    public CardRecyclerViewAdapter(CP creatorPool) {
        this(new ArrayList<>(), creatorPool);
    }

    public CardRecyclerViewAdapter(List<?> items) {
        mItems = items;
        mCreatorPool = onCreateCreatorPool();
    }

    public CardRecyclerViewAdapter(List<?> items, CP creatorPool) {
        mItems = items;
        mCreatorPool = creatorPool;
    }

    public abstract CP onCreateCreatorPool();

    public CP getCreatorPool() {
        return mCreatorPool;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getItems() {
        return mItems;
    }

    public void setItems(List items) {
        mItems = items;
    }

    @SuppressWarnings("unchecked")
    public <T> T getItemAt(int position) {
        return (T) mItems.get(position);
    }

    @Override
    public final int getItemCount() {
        return mItems.size();
    }

    @Override
    public final int getItemViewType(int position) {
        Object data = getItemAt(position);
        int index = mCreatorPool.getCreatorIndex(this, position);
        if (index >= 0) {
            if (DEBUG) {
                Log.d(TAG, "get creator index for position " + position + ": index=" + index + ", creator=" + mCreatorPool.getCreator(index).getClass().getSimpleName());
            }
            return index;
        }
        throw new IllegalStateException("Can't find Creator for " + data.getClass() + ", position: " + position);
    }

    public LayoutInflater onGetLayoutInflater(View parent) {
        return LayoutInflater.from(parent.getContext());
    }

    @NonNull
    @Override
    public final CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int creatorIndex) {
        LayoutInflater inflater = onGetLayoutInflater(parent);
        CardViewHolder.Creator creator = mCreatorPool.getCreator(creatorIndex);
        if (DEBUG) {
            Log.d(TAG, "create view holder for index " + creatorIndex + ": " + creator.getClass().getSimpleName());
        }
        return creator.createViewHolder(inflater, parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (DEBUG) {
            Log.d(TAG,
                    "bind: position=" + position
                            + ", holder=" + holder.getClass().getSimpleName()
                            + ", data=" + getItemAt(position)
                            + ", payloads=" + payloads);
        }
        if (!payloads.isEmpty()) {
            holder.bind(payloads, getItemAt(position), this);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
        holder.bind(getItemAt(position), this);
    }

    @Override
    public void onViewRecycled(@NonNull CardViewHolder holder) {
        super.onViewRecycled(holder);

        holder.recycle();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CardViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.onViewDetachedFromWindow();
    }
}
