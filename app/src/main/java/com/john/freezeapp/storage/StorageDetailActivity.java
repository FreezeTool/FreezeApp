package com.john.freezeapp.storage;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;

public class StorageDetailActivity extends ToolbarActivity {

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.storage_detail_name);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_detail);
    }
}
