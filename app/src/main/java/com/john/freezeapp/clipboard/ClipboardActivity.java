package com.john.freezeapp.clipboard;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.daemon.clipboard.ClipboardData;

import java.util.ArrayList;
import java.util.List;

public class ClipboardActivity extends ToolbarActivity {

    ClipboardAdapter mAdapter = new ClipboardAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        requestClipboardData();
    }

    private void requestClipboardData() {
        List<ClipboardData> clipDataList = Clipboard.getClipData();
        if (clipDataList != null) {
            List<ClipboardCardData> clipboardCardDataList = new ArrayList<>();
            for (ClipboardData clipData : clipDataList) {
                ClipboardCardData clipboardCardData = new ClipboardCardData();
                clipboardCardData.id = clipData.id;
                clipboardCardData.content = clipData.content;
                clipboardCardData.packageName = clipData.packageName;
                clipboardCardDataList.add(clipboardCardData);
            }
            mAdapter.updateData(clipboardCardDataList);
        }
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.clipboard_name);
    }

    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_clipboard_menu, menu);
        this.menu = menu;
        initMenu();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clipboard_switcher) {
            if (Clipboard.isMonitor()) {
                Clipboard.stop();
                initMenu();
            } else {
                Clipboard.start();
                initMenu();
            }
            return true;
        } else if (item.getItemId() == R.id.clipboard_clear) {
            showClearClipboardDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenu() {
        MenuItem item = menu.findItem(R.id.clipboard_switcher);
        item.setTitle(Clipboard.isMonitor() ? R.string.btn_close : R.string.btn_open);
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
}
