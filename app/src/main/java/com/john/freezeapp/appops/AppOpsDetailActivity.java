package com.john.freezeapp.appops;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.util.FreezeUtil;

import java.util.ArrayList;
import java.util.List;

public class AppOpsDetailActivity extends BaseActivity {

    public static final String KEY_PACKAGE = "KEY_PACKAGE";

    AppOpsAdapter mAdapter = new AppOpsAdapter(new AppOpsAdapter.OnItemClick() {
        @Override
        public void refreshAppOps(String packageName) {
            requestAppOps(packageName);
        }
    });

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
        Intent intent = getIntent();
        String packageName = intent.getStringExtra(KEY_PACKAGE);
        requestAppOps(packageName);
    }

    private void requestAppOps(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            List<AppOps.AppOpsDetail> appOpDetail = AppOps.getAppOpDetail(packageName);
            if (appOpDetail == null) {
                return;
            }
            List<CardData> items = mAdapter.getItems();
            if (items != null && !items.isEmpty()) {
                for (CardData cardData : items) {
                    if (cardData instanceof AppOpsDetailData) {
                        AppOpsDetailData item = (AppOpsDetailData) cardData;
                        AppOps.AppOpsDetail appOpsDetail = appOpDetail.stream().filter(appOpsDetail1 -> item.op == appOpsDetail1.op).findFirst().orElse(null);
                        if (appOpsDetail != null) {
                            item.pkgMode = appOpsDetail.pkgMode;
                            item.uidMode = appOpsDetail.uidMode;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                List<CardData> appOpsDetailLists = new ArrayList<>();
                AppOpsPackageDetailData packageDetailData = getAppOpsPackageDetailData(packageName);
                appOpsDetailLists.add(packageDetailData);
                for (AppOps.AppOpsDetail appOpsDetail : appOpDetail) {
                    AppOpsDetailData appOpsDetailData = new AppOpsDetailData(appOpsDetail);
                    appOpsDetailLists.add(appOpsDetailData);
                }
                mAdapter.updateData(appOpsDetailLists);
            }
        }

    }

    private AppOpsPackageDetailData getAppOpsPackageDetailData(String packageName) {
        AppOpsPackageDetailData appOpsPackageDetailData = new AppOpsPackageDetailData();
        appOpsPackageDetailData.packageName = packageName;
        try {
            ApplicationInfo applicationInfo;
            if (FreezeUtil.atLeast33()) {
                applicationInfo = ClientSystemService.getPackageManager().getApplicationInfo(packageName, 0L, 0);
            } else {
                applicationInfo = ClientSystemService.getPackageManager().getApplicationInfo(packageName, 0, 0);
            }

            appOpsPackageDetailData.uid = applicationInfo.uid;
            appOpsPackageDetailData.api = applicationInfo.targetSdkVersion;

            PackageInfo packageInfo;
            if (FreezeUtil.atLeast33()) {
                packageInfo = ClientSystemService.getPackageManager().getPackageInfo(packageName, 0L, 0);
            } else {
                packageInfo = ClientSystemService.getPackageManager().getPackageInfo(packageName, 0, 0);
            }

            appOpsPackageDetailData.versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appOpsPackageDetailData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
