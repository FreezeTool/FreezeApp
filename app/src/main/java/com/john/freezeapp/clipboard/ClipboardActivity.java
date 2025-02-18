package com.john.freezeapp.clipboard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.daemon.clipboard.ClipboardData;
import com.john.freezeapp.home.CommonEmptyData;
import com.john.freezeapp.recyclerview.CardData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClipboardActivity extends ToolbarActivity {

    ClipboardAdapter mAdapter = new ClipboardAdapter(new ClipboardAdapter.OnItemClick() {
        @Override
        public void onItemClick(CardData cardData) {
            if (cardData instanceof ClipboardCardData) {
                boolean copy = Clipboard.copy(((ClipboardCardData) cardData).id);
                Toast.makeText(getContext(), copy ? "复制成功" : "复制失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLongItemClick(CardData cardData) {
            if (cardData instanceof ClipboardCardData) {
                showRemoveClipboardDialog((ClipboardCardData) cardData);
            }
        }
    });

    RecyclerView recyclerView;
    Menu menu;
    TextView tvTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard);
        tvTip = findViewById(R.id.tip);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        updateData();
        Clipboard.addClipboardChange(new Clipboard.ClipboardChange() {
            @Override
            public void change() throws RemoteException {
                updateData();
            }
        });
    }

    private void updateData() {
        postUI(new Runnable() {
            @Override
            public void run() {
                requestClipboardData();
            }
        });
    }

    private void requestClipboardData() {
        List<ClipboardData> clipDataList = Clipboard.getClipData();
        List<CardData> clipboardCardDataList = new ArrayList<>();
        if (clipDataList != null && !clipDataList.isEmpty()) {
            Collections.sort(clipDataList);
            for (ClipboardData clipData : clipDataList) {
                ClipboardCardData clipboardCardData = new ClipboardCardData();
                clipboardCardData.id = clipData.id;
                clipboardCardData.content = clipData.content;
                clipboardCardData.packageName = clipData.packageName;
                clipboardCardData.timestamp = clipData.timestamp;
                clipboardCardDataList.add(clipboardCardData);
            }
        } else {
            CommonEmptyData commonEmptyData = new CommonEmptyData();
            commonEmptyData.type = CommonEmptyData.TYPE_EMPTY;
            commonEmptyData.content = getContext().getString(R.string.main_home_empty_content);
            commonEmptyData.height = recyclerView.getMeasuredHeight();
            clipboardCardDataList.add(commonEmptyData);
        }
        mAdapter.updateData(clipboardCardDataList);
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.clipboard_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_clipboard_menu, menu);
        this.menu = menu;
        initUI();
        initClipboardFloating();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clipboard_switcher) {
            if (Clipboard.isMonitor()) {
                Clipboard.stop();
                initUI();
            } else {
                Clipboard.start();
                initUI();
            }
            return true;
        } else if (item.getItemId() == R.id.clipboard_clear) {
            showClearClipboardDialog();
            return true;
        } else if (item.getItemId() == R.id.clipboard_floating) {
            if (Clipboard.isClipboardFloating()) {
                Clipboard.setClipboardFloating(false);
                Clipboard.stopClipboardFloating(getContext());
            } else {
                Clipboard.setClipboardFloating(true);
                Clipboard.startClipboardFloating(getContext());
            }
            initClipboardFloating();
        }


        return super.onOptionsItemSelected(item);
    }

    private void initClipboardFloating() {
        MenuItem item = menu.findItem(R.id.clipboard_floating);
        if (Clipboard.isClipboardFloating()) {
            item.setTitle("关闭悬浮球");
        } else {
            item.setTitle("开启悬浮球");
        }
    }

    private void initUI() {
        MenuItem item = menu.findItem(R.id.clipboard_switcher);
        boolean isMonitor = Clipboard.isMonitor();
        item.setTitle(isMonitor ? R.string.btn_close : R.string.btn_open);
        tvTip.setText(isMonitor ? "粘贴板监控服务已开启。" : "粘贴板监控服务未开启，请先点击\"开启\"。");
    }


    private void showClearClipboardDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("清空所有粘贴板记录")
                .setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Clipboard.clear();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


    private void showRemoveClipboardDialog(ClipboardCardData cardData) {
        new AlertDialog.Builder(getContext())
                .setMessage("确认要删除吗？")
                .setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Clipboard.remove(cardData.id);
                        requestClipboardData();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
