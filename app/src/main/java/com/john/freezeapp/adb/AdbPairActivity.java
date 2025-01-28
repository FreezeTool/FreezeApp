package com.john.freezeapp.adb;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardData;

import java.util.ArrayList;
import java.util.List;

public class AdbPairActivity extends BaseActivity {

    AdbPairAdapter mAdapter = new AdbPairAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adb_pair);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        updateData();
    }

    private void updateData() {
        List<CardData> list = new ArrayList<>();

        AdbPairData adbPairTipData = new AdbPairData();
        adbPairTipData.icon = R.drawable.ic_vector_check;
        adbPairTipData.subTitle = "一段来自Freeze的通知将帮助您完成配对。";
        list.add(adbPairTipData);

        AdbPairData adbPairTipData2 = new AdbPairData();
        adbPairTipData2.icon = R.drawable.ic_vector_error;
        adbPairTipData2.subTitle = "Freeze 需要访问本地网络。它由网络权限控制。\n某些系统（例如MIUI）不允许应用在不可见时访问网络，及时应用已按标准使用前台服务。请在此类系统上为Freeze 禁用电池优化功能。";
        list.add(adbPairTipData2);


        AdbPairData developPairData = new AdbPairData();
        developPairData.icon = R.drawable.ic_vector_error;
        developPairData.subTitle = "进入“开发者选项” - “无线调试”。点按“使用配对码配对设备”，您将看到一个六位数字代码。";

        AdbPairData.AdbPairBtnData rightBtnData = new AdbPairData.AdbPairBtnData();
        rightBtnData.text = "开发者选项";
        rightBtnData.icon = R.drawable.ic_vector_link;
        rightBtnData.show = true;
        rightBtnData.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDevelop();
            }
        };
        developPairData.rightBtnData = rightBtnData;
        list.add(developPairData);


        mAdapter.updateData(list);
    }

    private void toDevelop() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
