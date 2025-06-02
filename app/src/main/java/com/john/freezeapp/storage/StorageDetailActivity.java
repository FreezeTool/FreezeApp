package com.john.freezeapp.storage;

import android.app.Activity;
import android.app.usage.StorageStats;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.util.CommonUtil;
import com.john.freezeapp.util.FreezeUtil;

@RequiresApi(Build.VERSION_CODES.O)
public class StorageDetailActivity extends ToolbarActivity {
    public static final String KEY_DATA = "KEY_DATA";
    private TextView tvTotal, tvCode, tvUserData, tvCache;
    private AppCompatButton btnClearAll, btnClearCache, btnManager;

    public static void start(Activity context, StorageData data) {
        Intent intent = new Intent(context, StorageDetailActivity.class);
        intent.putExtra(KEY_DATA, data);
        context.startActivityForResult(intent, StorageActivity.KEY_REQUEST_CODE);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.storage_detail_name);
    }

    StorageData mStorageData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_detail);
        Intent intent = getIntent();
        Parcelable parcelable = intent.getParcelableExtra(KEY_DATA);
        if (!(parcelable instanceof StorageData)) {
            finish();
            return;
        }

        mStorageData = (StorageData) parcelable;
        tvTotal = findViewById(R.id.tv_total);
        tvCode = findViewById(R.id.tv_code);
        tvUserData = findViewById(R.id.tv_user_data);
        tvCache = findViewById(R.id.tv_cache);
        btnClearAll = findViewById(R.id.btn_clear_all);
        btnClearAll.setOnClickListener(v -> {

        });
        btnClearCache = findViewById(R.id.btn_clear_cache);
        btnClearCache.setOnClickListener(v -> {

        });
        btnManager = findViewById(R.id.btn_manager);
        btnManager.setOnClickListener(v -> {
            FreezeUtil.toPackageSetting(getContext(), mStorageData.packageName);
        });
        updateUI(mStorageData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        Storage.requestAppSize(getContext(), mStorageData.packageName, new Storage.Callback3() {
            @Override
            public void success(String packageName, StorageStats storageStats) {
                postUI(new Runnable() {
                    @Override
                    public void run() {
                        mStorageData.setStorageStats(storageStats);
                        updateUI(mStorageData);
                        Intent intent = new Intent();
                        intent.putExtra(KEY_DATA, mStorageData);
                        setResult(RESULT_OK, intent);
                    }
                });
            }

            @Override
            public void fail() {

            }
        });
    }

    private void updateUI(StorageData data) {
        postUI(new Runnable() {
            @Override
            public void run() {
                tvCache.setText(CommonUtil.getSizeText(data.cacheBytes));
                tvUserData.setText(CommonUtil.getSizeText(data.dataBytes));
                tvCode.setText(CommonUtil.getSizeText(data.codeBytes));
                tvTotal.setText(CommonUtil.getSizeText(data.cacheBytes + data.dataBytes + data.codeBytes));
            }
        });
    }
}
