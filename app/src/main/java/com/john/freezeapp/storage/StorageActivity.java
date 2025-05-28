package com.john.freezeapp.storage;

import android.app.AppOpsManager;
import android.app.AppOpsManagerHidden;
import android.app.usage.StorageStats;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarSearchActivity;
import com.john.freezeapp.appops.AppOps;
import com.john.freezeapp.recyclerview.CardData;
import com.john.freezeapp.util.DeviceUtil;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ThreadPool;
import com.john.freezeapp.util.UIExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiresApi(Build.VERSION_CODES.O)
public class StorageActivity extends ToolbarSearchActivity {

    StorageAdapter mAdapter = new StorageAdapter(new StorageAdapter.OnItemClick() {
        @Override
        public void onItemClick(CardData cardData) {
            if (cardData instanceof StorageData) {
                StorageDetailActivity.start(StorageActivity.this, (StorageData) cardData);
            }

        }
    });

    public static final int KEY_REQUEST_CODE = 100;

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.storage_name);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        if (DeviceUtil.atLeast30()) {
            AppOps.setUidMode(AppOpsManagerHidden.OP_MANAGE_EXTERNAL_STORAGE, AppOpsManager.MODE_ALLOWED, Process.myUid(), BuildConfig.APPLICATION_ID);
        }
        AppOps.setUidMode(AppOpsManagerHidden.OP_GET_USAGE_STATS, AppOpsManager.MODE_ALLOWED, Process.myUid(), BuildConfig.APPLICATION_ID);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        requestStorageApp(getContext());
    }

    List<StorageData> mStorageDataList;

    private void requestStorageApp2(Context context) {
        showLoading();
        Storage.requestStorageStats(context, new Storage.Callback2() {
            @Override
            public void success(List<Storage.Model> models) {
                if (isDestroy()) {
                    return;
                }
                hideLoading();
                if (models != null && !models.isEmpty()) {
                    List<StorageData> storageDataList = new ArrayList<>();
                    for (Storage.Model model : models) {
                        StorageData storageData = new StorageData(model.packageName, model.packageInfo, model.storageStats);
                        storageDataList.add(storageData);
                    }
                    Collections.sort(storageDataList);
                    UIExecutor.postUI(() -> {
                        mStorageDataList = storageDataList;
                        updateData();
                    });
                }
            }

            @Override
            public void fail() {
                if (isDestroy()) {
                    return;
                }
                hideLoading();
            }
        });
    }

    private void updateData() {
        if (mStorageDataList != null) {
            String query = getQuery();
            if (TextUtils.isEmpty(query)) {
                UIExecutor.postUI(() -> mAdapter.updateData(mStorageDataList));
            } else {
                List<StorageData> list = new ArrayList<>(mStorageDataList);
                ThreadPool.execute(() -> {
                    List<StorageData> storageDataList = new ArrayList<>();
                    for (StorageData storageData : list) {
                        FreezeAppManager.CacheAppModel appModel = FreezeAppManager.getAppModel(getContext(), storageData.packageName);
                        if (appModel.name != null && appModel.name.toLowerCase().contains(query.toLowerCase())) {
                            storageDataList.add(storageData);
                        }
                    }
                    UIExecutor.postUI(() -> mAdapter.updateData(storageDataList));
                });
            }
        }
    }

    private void requestStorageApp(Context context) {
        findViewById(R.id.tip).setVisibility(View.VISIBLE);
        showLoading();
        Storage.requestStorageStats(context, new Storage.Callback() {
            @Override
            public void updateStorageStat(String packageName, StorageStats storageStats) {
                if (isDestroy()) {
                    return;
                }
                hideLoading();
                StorageData storageData = getStorageData(packageName);
                if (storageData != null) {
                    storageData.setStorageStats(storageStats);
                }
                UIExecutor.postUI(() -> {
                    updateData();
                });
            }

            @Override
            public void updatePackageInfo(List<PackageInfo> packageInfos) {
                if (isDestroy()) {
                    return;
                }
                hideLoading();
                if (packageInfos != null) {
                    List<StorageData> storageDataList = new ArrayList<>();
                    for (PackageInfo packageInfo : packageInfos) {
                        StorageData storageData = new StorageData(packageInfo.packageName, packageInfo, null);
                        storageDataList.add(storageData);
                    }
                    mStorageDataList = storageDataList;
                }
                UIExecutor.postUI(() -> {
                    updateData();
                });
            }

            @Override
            public void complete() {
                if (isDestroy()) {
                    return;
                }
                hideLoading();
                if (mStorageDataList != null) {
                    Collections.sort(mStorageDataList);
                    updateData();
                }
                postUI(() -> findViewById(R.id.tip).setVisibility(View.GONE));
            }

            @Override
            public void fail() {
                if (isDestroy()) {
                    return;
                }
                hideLoading();
            }
        });
    }

    private StorageData getStorageData(String packageName) {
        if (mStorageDataList != null) {
            for (StorageData storageData : mStorageDataList) {
                if (TextUtils.equals(storageData.packageName, packageName)) {
                    return storageData;
                }
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == KEY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Parcelable parcelable = data.getParcelableExtra(StorageDetailActivity.KEY_DATA);
            if (parcelable instanceof StorageData) {
                for (StorageData storageData : mStorageDataList) {
                    if (TextUtils.equals(storageData.packageName, ((StorageData) parcelable).packageName)) {
                        storageData.setStorageStats(((StorageData) parcelable).storageStats);
                    }
                }
                updateData();
            }

        }
    }

    @Override
    protected void onQueryTextClose() {
        super.onQueryTextClose();
        updateData();
    }

    @Override
    protected void onQueryTextChange(String query) {
        super.onQueryTextChange(query);
        updateData();
    }
}
