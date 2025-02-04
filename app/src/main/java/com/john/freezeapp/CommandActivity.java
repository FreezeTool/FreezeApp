package com.john.freezeapp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientRemoteShell;

public class CommandActivity extends BaseActivity {
    Toolbar toolbar;
    EditText etCommand;
    Button btnSend;
    TextView tvCommandResult;
    FrameLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        if (!isDaemonActive()) {
            finish();
            return;
        }

        mContainer = findViewById(R.id.fl_container);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etCommand = findViewById(R.id.et_command);
        btnSend = findViewById(R.id.command_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = etCommand.getText().toString().trim();
                if (TextUtils.isEmpty(command)) {
                    return;
                }
                hideSoftInput();
                executeCommand2(command);
            }
        });
        tvCommandResult = findViewById(R.id.command_result);
        tvCommandResult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideSoftInput();
                }
                return false;
            }
        });
    }


    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCommand.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        finish();
    }

    private void executeCommand2(String command) {
        if (ClientBinderManager.isActive()) {
            showLoading();
            tvCommandResult.setText("");
            ClientRemoteShell.execCommand(command, new ClientRemoteShell.RemoteShellCommandResultCallback() {
                @Override
                public void callback(ClientRemoteShell.RemoteShellCommandResult result) {
                    hideLoading();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(result.successMsg)) {
                                tvCommandResult.setText(result.successMsg);
                            } else if (!TextUtils.isEmpty(result.errorMsg)) {
                                tvCommandResult.setText(result.errorMsg);
                            }

                        }
                    });
                }
            });

        }
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

    @Override
    protected ViewGroup getLoadingContainer() {
        return mContainer;
    }
}
