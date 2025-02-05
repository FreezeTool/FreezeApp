package com.john.freezeapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public abstract class ToolbarActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setContentView(View view) {
        ViewGroup container = getContainer(getContext());
        container.addView(view);
        super.setContentView(container);
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup container = getContainer(getContext());
        LayoutInflater.from(getContext()).inflate(layoutResID, container, true);
        super.setContentView(container);
    }

    private ViewGroup getContainer(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Toolbar toolbar = new Toolbar(context);
        toolbar.setTitle(getToolbarTitle());
        initToolbar(toolbar);
        linearLayout.addView(toolbar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return linearLayout;
    }

    protected void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected abstract String getToolbarTitle();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (interceptToolbarBack()) {
                return true;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean interceptToolbarBack() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (interceptToolbarBack()) {
            return;
        }
        super.onBackPressed();
    }

}
