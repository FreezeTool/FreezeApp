package com.john.freezeapp.fs;

import android.app.AppOpsManager;
import android.app.AppOpsManagerHidden;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.appops.AppOps;
import com.john.freezeapp.common.CommonAdapter;
import com.john.freezeapp.runas.ClientRunAs;
import com.john.freezeapp.runas.RunAsModel;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.UIExecutor;

import java.util.List;

public class FileServerActivity extends ToolbarActivity {

    TextView mTvUrl;
    Button btnFS;

    RecyclerView recyclerView;
    CommonAdapter mCommonAdapter = new CommonAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_server);

        AppOps.setUidMode(AppOpsManagerHidden.OP_WRITE_CLIPBOARD, AppOpsManager.MODE_ALLOWED, Process.myUid(), BuildConfig.APPLICATION_ID);

        if (!isDaemonActive()) {
            finish();
            return;
        }
        mCommonAdapter.setListener((CommonAdapter.ItemListener) object -> {
            if (object instanceof RunAsModel) {
                showLoading();
                if (((RunAsModel) object).runAsProcessModel.active) {
                    ClientRunAs.stopServer(((RunAsModel) object).runAsProcessModel.packageName);
                } else {
                    ClientRunAs.startServer(getContext(), ((RunAsModel) object).runAsProcessModel.packageName);
                }
                UIExecutor.postDelay(() -> {
                    hideLoading();
                    updateRunAsModel();
                }, 100);
            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mCommonAdapter);

        mTvUrl = findViewById(R.id.tv_url);
        mTvUrl.setOnClickListener(view -> {
            if (ClientFileServer.isActive()) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    clipboardManager.setText(ClientFileServer.getAccessUrl());
                    FreezeUtil.showShortToast("复制成功");
                }
            }
        });

        TextView tvInternalAppUrl = findViewById(R.id.tv_internal_app_url);
        tvInternalAppUrl.setText(ClientRunAs.getInternalAppFileServerUrl());
        tvInternalAppUrl.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboardManager != null) {
                clipboardManager.setText(tvInternalAppUrl.getText());
                FreezeUtil.showShortToast("复制成功");
            }
        });

        btnFS = findViewById(R.id.btn_file_server);
        btnFS.setOnClickListener(view -> {
            showLoading();
            if (ClientFileServer.isActive()) {
                ClientFileServer.stopServer();
            } else {
                ClientFileServer.startServer();
            }
            UIExecutor.postDelay(() -> {
                hideLoading();
                updateUI();
            }, 200);
        });
        updateUI();
        updateRunAsModel();
    }

    private void updateRunAsModel() {
        List<RunAsModel> runAsModel = ClientRunAs.getRunAsModel();
        mCommonAdapter.updateData(runAsModel);
    }

    private void updateUI() {
        String accessUrl = ClientFileServer.getAccessUrl();
        mTvUrl.setText(accessUrl == null ? "暂未启动FileServer" : accessUrl);
        btnFS.setText(ClientFileServer.isActive() ? R.string.btn_stop_file_server : R.string.btn_start_file_server);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.app_file_server);
    }
}
