package com.john.freezeapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import org.json.JSONObject;

public class CommandActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText etCommand;
    Button btnSend;
    TextView tvCommandResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
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
                executeCommand(command);
            }
        });
        tvCommandResult = findViewById(R.id.command_result);
    }

    private void executeCommand(String command) {
        ShellClient.command(command, new ShellClient.Callback() {
            @Override
            public void success(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int code = jsonObject.optInt("code", 0);
                    if (code == 0) {
                        String result = jsonObject.optString("data", "");
                        if (!TextUtils.isEmpty(result)) {

                            Gson gson = new Gson();
                            ShellUtils.ShellCommandResult commandResult = gson.fromJson(result, ShellUtils.ShellCommandResult.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!TextUtils.isEmpty(commandResult.successMsg)) {
                                        tvCommandResult.setText(commandResult.successMsg);
                                    } else if (!TextUtils.isEmpty(commandResult.errorMsg)) {
                                        tvCommandResult.setText(commandResult.errorMsg);
                                    }

                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail() {

            }
        });
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
