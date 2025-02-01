package com.john.freezeapp.appops;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.client.ClientLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppOpsDetailActivity extends BaseActivity {

    public static final String KEY_PACKAGE = "KEY_PACKAGE";

    AppOpsAdapter mAdapter = new AppOpsAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_ops_detail);

        if (!isDaemonActive()) {
            finish();
            return;
        }

        initToolbar();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        requestAppOps();
    }

    private void requestAppOps() {
        Intent intent = getIntent();
        String packageName = intent.getStringExtra(KEY_PACKAGE);

        if (!TextUtils.isEmpty(packageName)) {
            List<AppOps.AppOpsDetail> appOpDetail = AppOps.getAppOpDetail(packageName);
            if (appOpDetail != null) {
                List<AppOpsDetailData> appOpsDetailLists = new ArrayList<>();
                for (AppOps.AppOpsDetail appOpsDetail : appOpDetail) {
                    AppOpsDetailData appOpsDetailData = new AppOpsDetailData(appOpsDetail);
                    appOpsDetailLists.add(appOpsDetailData);
                }
//                Collections.sort(appOpsDetailLists);
                mAdapter.updateData(appOpsDetailLists);
            }
        }

    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 在这里处理返回按钮的点击事件
                finish(); // 或者其他你想要执行的操作
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
