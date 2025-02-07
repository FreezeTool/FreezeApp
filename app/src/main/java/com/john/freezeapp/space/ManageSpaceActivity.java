package com.john.freezeapp.space;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;

public class ManageSpaceActivity extends ToolbarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_space);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.manage_space_name);
    }
}
