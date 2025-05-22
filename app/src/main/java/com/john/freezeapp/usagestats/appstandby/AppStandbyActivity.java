package com.john.freezeapp.usagestats.appstandby;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarSearchActivity;
import com.john.freezeapp.clipboard.Clipboard;
import com.john.freezeapp.usagestats.UsageStatsAdapter;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.ScreenUtils;
import com.john.freezeapp.util.ThreadPool;
import com.john.freezeapp.util.UIExecutor;

import java.util.ArrayList;
import java.util.List;

public class AppStandbyActivity extends ToolbarSearchActivity {

    RecyclerView recyclerView;
    UsageStatsAdapter mAdapter = new UsageStatsAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_standby);

        if (!isDaemonActive()) {
            finish();
            return;
        }
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        findViewById(R.id.tip).setOnClickListener(v -> {
            showAppStandbyBucketDialog();
        });
        requestAppStandbyBucket();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.app_standby_name);
    }

    private void updateData() {
        if (isDestroy()) {
            return;
        }
        if (mAppStandbyList != null) {
            String query = getQuery();
            if (TextUtils.isEmpty(query)) {
                UIExecutor.postUI(() -> mAdapter.updateData(mAppStandbyList));
            } else {
                List<AppStandbyData> list = new ArrayList<>(mAppStandbyList);
                ThreadPool.execute(() -> {
                    List<AppStandbyData> queryLists = new ArrayList<>();
                    for (AppStandbyData usageStatsData : list) {
                        FreezeAppManager.CacheAppModel appModel = FreezeAppManager.getAppModel(getContext(), usageStatsData.packageName);
                        if (!TextUtils.isEmpty(appModel.name) && appModel.name.toLowerCase().contains(query.toLowerCase())) {
                            queryLists.add(usageStatsData);
                        }
                    }
                    UIExecutor.postUI(() -> mAdapter.updateData(queryLists));
                });
            }
        }
    }

    List<AppStandbyData> mAppStandbyList;

    private void requestAppStandbyBucket() {
        showLoading();
        AppStandby.requestAppStandbyBucket(new AppStandby.Callback() {
            @Override
            public void success(List<AppStandbyData> appStandbyList) {
                hideLoading();
                if (isDestroy()) {
                    return;
                }
                List<PackageInfo> installApp = FreezeAppManager.getInstallApp(FreezeAppManager.TYPE_NORMAL_APP, FreezeAppManager.STATUS_ALL, true);
                List<AppStandbyData> installAppStandbyData = new ArrayList<>();
                for (AppStandbyData appStandbyData : appStandbyList) {
                    if (installApp.stream().anyMatch(packageInfo -> TextUtils.equals(packageInfo.packageName, appStandbyData.packageName))) {
                        installAppStandbyData.add(appStandbyData);
                    }
                }
                mAppStandbyList = installAppStandbyData;
                updateData();
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        finish();
    }

    @Override
    protected void onQueryTextChange(String query) {
        super.onQueryTextChange(query);
        updateData();
    }

    @Override
    protected void onQueryTextClose() {
        super.onQueryTextClose();
        updateData();
    }

    private static List<Pair<String, String>> sAppStandbyBucketKnowledges = new ArrayList<>();

    static {
        sAppStandbyBucketKnowledges.add(new Pair<>("豁免", "应用由于某些原因被豁免，并且无法更改存储桶。"));
        sAppStandbyBucketKnowledges.add(new Pair<>("活跃", "应用目前正在使用或不久前才被使用过。"));
        sAppStandbyBucketKnowledges.add(new Pair<>("工作集", "应用会被定期使用。"));
        sAppStandbyBucketKnowledges.add(new Pair<>("常用", "应用经常被使用，但不是每天都使用。极少使用：应用不经常被使用。"));
        sAppStandbyBucketKnowledges.add(new Pair<>("极少使用", "应用不经常被使用。"));
        sAppStandbyBucketKnowledges.add(new Pair<>("受限", "应用会消耗大量的系统资源，或可能出现不希望出现的行为。"));
        sAppStandbyBucketKnowledges.add(new Pair<>("从未使用", "已安装但从未运行过的应用，系统会对这些应用施加严格的限制。"));
    }

    private void showAppStandbyBucketDialog() {
        int color = getColor(R.color.colorPrimary);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("系统会动态地将每个应用分配到一个优先级分桶，并根据需要重新分配应用。系统可能依赖于某个预加载的应用，该应用使用机器学习技术判断每个应用将被使用的可能性，并将应用分配到相应的分桶。 <br><br>");

        for (int i = 0; i < sAppStandbyBucketKnowledges.size(); i++) {
            Pair<String, String> pair = sAppStandbyBucketKnowledges.get(i);
            stringBuilder.append("<font color=\"").append(color).append("\"><b>").append(pair.first).append("</b></font>:").append(pair.second);
            if (i != sAppStandbyBucketKnowledges.size() - 1) {
                stringBuilder.append("<br>");
            }
        }

        new AlertDialog.Builder(getContext())
                .setTitle("优先级分桶")
                .setMessage(Html.fromHtml(stringBuilder.toString()))
                .setPositiveButton(R.string.btn_submit, (dialog, which) -> {
                })
                .show();
    }
}
