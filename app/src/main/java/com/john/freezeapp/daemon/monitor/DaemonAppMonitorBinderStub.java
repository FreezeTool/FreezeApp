package com.john.freezeapp.daemon.monitor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.RemoteException;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import com.john.freezeapp.App;
import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.DaemonLog;
import com.john.freezeapp.monitor.AppMonitorActivity;
import com.john.freezeapp.util.ScreenUtils;
import com.john.freezeapp.window.FloatWindow;

public class DaemonAppMonitorBinderStub extends IDaemonAppMonitorBinder.Stub {

    FloatWindow mFloatWindow;
    TextView mTextView;

    private void showWindow(Context context) {
        if (mFloatWindow == null) {
            FloatWindow floatWindow = new FloatWindow(context);
            floatWindow.setOnLongClickListener(v -> {
                Intent intent = new Intent(context, AppMonitorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApp().startActivity(intent);
                return false;
            });
            TextView textView = new TextView(context);
            int padding = ScreenUtils.dp2px(context, 10);
            textView.setPadding(padding, padding, padding, padding);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
//            mTextView.setBackgroundResource(R.drawable.mask_background);
            textView.setBackgroundColor(0x80000000);
            textView.setText(getShowContent("1111", "111"));
            floatWindow.setView(textView);
            mTextView = textView;
            mFloatWindow = floatWindow;

        }
        mFloatWindow.show();
    }

    private String getShowContent(String packageName, String className) {
        return packageName +
                "\n" +
                className;
    }

    private void updateTextViewSize(int size) {
        if (mTextView != null) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
    }

    private void hideWindow() {
        if (mFloatWindow != null) {
            mFloatWindow.hide();
        }
    }


    @Override
    public void start() throws RemoteException {
        try {
            showWindow(Daemon.getDaemon().mActivityThread.getApplication());
        } catch (Exception e) {
            DaemonLog.e(e, "DaemonAppMonitorBinderStub show");
        }
    }


    @Override
    public void stop() throws RemoteException {
        try {
            hideWindow();
        } catch (Exception e) {

        }
    }

    @Override
    public void update(DaemonAppMonitorConfig config) throws RemoteException {

        try {
            if (config != null) {
                updateTextViewSize(config.size);
            }
        } catch (Exception e) {

        }
    }
}
