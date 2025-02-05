package com.john.freezeapp.storage;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarSearchActivity;

public class StorageActivity extends ToolbarSearchActivity {

    StorageAdapter mAdapter = new StorageAdapter();

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.storage_name);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }
}
