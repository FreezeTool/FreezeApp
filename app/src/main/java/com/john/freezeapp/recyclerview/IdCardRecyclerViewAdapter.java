package com.john.freezeapp.recyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IdCardRecyclerViewAdapter extends CardRecyclerViewAdapter<IndexCreatorPool> {

    private final List<Long> mIds = new ArrayList<>();

    public IdCardRecyclerViewAdapter() {
        super();
    }

    public IdCardRecyclerViewAdapter(@NonNull List<Object> data) {
        super(data);
    }

    @NonNull
    public List<Long> getIds() {
        return mIds;
    }

    @Override
    public long getItemId(int position) {
        return mIds.get(position);
    }

    public void clear() {
        getCreatorPool().clear();
        getItems().clear();
        getIds().clear();
    }

    public <T> void addItemAt(int index, @NonNull CardViewHolder.Creator<T> creator, @Nullable T object, long id) {
        getCreatorPool().add(index, creator);
        getItems().add(index, object);
        getIds().add(index, id);
    }

    public <T> void addItem(@NonNull CardViewHolder.Creator<T> creator, @Nullable T object, long id) {
        getCreatorPool().add(creator);
        getItems().add(object);
        getIds().add(id);
    }

    public <T> void addItemsAt(int index, @NonNull CardViewHolder.Creator<T> creator, @NonNull List<T> list, @NonNull List<Long> ids) {
        for (int i = 0; i < list.size(); i++) {
            getCreatorPool().add(index, creator);
        }

        getItems().addAll(index, list);
        getIds().addAll(index, ids);
    }

    public <T> void addItems(@NonNull CardViewHolder.Creator<T> creator, @NonNull List<T> list, @NonNull List<Long> ids) {
        for (int index = 0; index < list.size(); index++) {
            addItem(creator, list.get(index), ids.get(index));
        }
    }

    public void removeItemAt(int index) {
        getCreatorPool().remove(index);
        getItems().remove(index);
        getIds().remove(index);
    }

    public void notifyItemChangeById(long targetId) {
        for (int index = 0; index < getItemCount(); index++) {
            if (getIds().get(index) == targetId) {
                notifyItemChanged(index);
            }
        }
    }

    public void notifyItemChangeById(long targetId, @Nullable Object payload) {
        for (int index = 0; index < getItemCount(); index++) {
            if (getIds().get(index) == targetId) {
                notifyItemChanged(index, payload);
            }
        }
    }

    public void setFirstItemById(long targetId, @Nullable Object object) {
        for (int index = 0; index < getItemCount(); index++) {
            if (getIds().get(index) == targetId) {
                getItems().set(index, object);
                return;
            }
        }
        throw new NoSuchElementException("Cannot found any items belongs to id=" + targetId);
    }

    @Override
    public IndexCreatorPool onCreateCreatorPool() {
        return new IndexCreatorPool();
    }

}
