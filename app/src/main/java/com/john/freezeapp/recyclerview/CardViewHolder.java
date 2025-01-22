package com.john.freezeapp.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

@SuppressWarnings("rawtypes")
public class CardViewHolder<T> extends RecyclerView.ViewHolder {

    public interface Creator<T> {
        CardViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent);
    }

    private T mData;
    private CardRecyclerViewAdapter mAdapter;

    public CardViewHolder(View itemView) {
        super(itemView);
    }

    @NonNull
    public final Context getContext() {
        return itemView.getContext();
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        setData(data, null);
    }

    public void setData(T data, Object payload) {
        mData = data;

        int position = getAdapterPosition();
        //noinspection unchecked
        getAdapter().getItems().set(position, data);
        getAdapter().notifyItemChanged(position, payload);
    }

    public CardRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    public final void bind(@NonNull List<Object> payloads, T data, CardRecyclerViewAdapter adapter) {
        mAdapter = adapter;
        mData = data;

        onBind(payloads);
    }

    public final void bind(T data, CardRecyclerViewAdapter adapter) {
        mAdapter = adapter;
        mData = data;

        onBind();
    }

    /**
     * Called when bind.
     *
     **/
    public void onBind() {

    }

    /**
     * Called when partial bind.
     *
     * @param payloads A non-null list of merged payloads
     */
    public void onBind(@NonNull List<Object> payloads) {

    }

    public final void recycle() {
        onRecycle();

        mData = null;
        mAdapter = null;
    }

    public void onRecycle() {

    }

    public void onViewAttachedToWindow() {

    }

    public void onViewDetachedFromWindow() {

    }
}
