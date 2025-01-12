package com.john.freezeapp;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    TextView tvContent, tvServer;
    LinearLayout llStartServer, llActiveServer;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvContent = findViewById(R.id.tv_content);
        tvServer = findViewById(R.id.tv_server);
        llStartServer = findViewById(R.id.ll_start_server);
        llActiveServer = findViewById(R.id.ll_active_server);
    }

    public void toManager(View v) {
        Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void toCommand(View v) {
        Intent intent = new Intent(MainActivity.this, CommandActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvServer.setVisibility(View.GONE);
        llStartServer.setVisibility(View.GONE);
        llActiveServer.setVisibility(View.GONE);
        checkBind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (R.id.menu_stop_server == itemId) {
            stopServer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stopServer() {
        ShellClient.stop(new ShellClient.Callback() {
            @Override
            public void success(String data) {
                Log.d("song", data);
                checkBind();
            }

            @Override
            public void fail() {
                checkBind();
            }
        });
    }


    private String getShellFilePath() {
        File externalFilesDir = getExternalFilesDir(null);
        externalFilesDir.mkdirs();
        File file = new File(externalFilesDir.getAbsolutePath() + "/start.sh");
        return file.getAbsolutePath();
    }

    private void generateShell() {
        String shellFilePath = getShellFilePath();
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(shellFilePath);
            printWriter.println("am force-stop " + getPackageName());
            printWriter.println(String.format("nohup app_process -Djava.class.path=%s /system/bin %s %s > /dev/null 2>&1 &", getApplicationInfo().sourceDir, Main.class.getName(), getPackageName()));
            printWriter.println("am start -a com.cleanmaster.hook.LAUNCH -p " + getApplicationContext().getPackageName() + " > /dev/null 2>&1 ;");
            printWriter.println("echo success");
            printWriter.close();
            ShellUtils.execCommand("chmod a+r " + shellFilePath, false);
        } catch (Throwable th) {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        }
    }

    public void showGuide() {

        llStartServer.setVisibility(View.VISIBLE);
        String shellFilePath = getShellFilePath();
        tvContent.setText(String.format("$ adb shell \n$ sh %s", shellFilePath));
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(shellFilePath);
            }
        });

    }

    private void showServerUnRunning() {
        tvServer.setVisibility(View.VISIBLE);
        tvServer.setText(R.string.main_app_server_not_active);
        llActiveServer.setVisibility(View.GONE);
    }

    private void showServerRunning(String task) {
        tvServer.setVisibility(View.VISIBLE);
        tvServer.setText(getString(R.string.main_app_server_active) + " " + task);
        tvContent.setOnClickListener(null);
        llActiveServer.setVisibility(View.VISIBLE);
    }


    private void checkBind() {
        ShellClient.bind(new ShellClient.Callback() {
            @Override
            public void success(String data) {
                Log.d("song", data);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int commandActiveCount = 0;
                        int commandTotalCount = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int code = jsonObject.optInt("code", -1);
                            if (code == 0) {
                                JSONObject dataObject = jsonObject.optJSONObject("data");
                                if (dataObject != null) {
                                    commandActiveCount = dataObject.optInt("command_active_count", 0);
                                    commandTotalCount = dataObject.optInt("command_total_count", 0);
                                }
                            }
                        } catch (Exception e) {
                            //
                        }
                        showServerRunning(String.format("(%d/%d)", commandActiveCount, commandTotalCount));
                    }
                });
            }

            @Override
            public void fail() {
                Log.d("song", "bind fail");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        generateShell();
                        showServerUnRunning();
                        showGuide();
                    }
                });
            }
        });
    }
}
