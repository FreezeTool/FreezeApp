package com.john.freezeapp.window;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.john.freezeapp.util.ScreenUtils;

public class FloatWindow implements IFloatWindow {
    private final WindowManager.LayoutParams mLayoutParams;
    private final FloatContainer mRootView;
    private final int mScreenHeight;
    private final int mScreenWidth;
    private final WindowManager mWindowManager;
    private boolean isShow = false;

    public View.OnLongClickListener onLongClickListener;
    public View.OnClickListener onClickListener;

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public FloatWindow(Context context) {

        mRootView = new FloatContainer(context);
        mRootView.setOnFloatListener(new FloatContainer.OnFloatListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onMove(float x, float y) {
                updateMovePosition((int) x, (int) y);
            }

            @Override
            public void onEnd() {

            }

            @Override
            public void onLongClick() {
                if (onLongClickListener != null) {
                    onLongClickListener.onLongClick(mRootView);
                }
            }

            @Override
            public void onClick() {
                if (onClickListener != null) {
                    onClickListener.onClick(mRootView);
                }
            }
        });
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mLayoutParams.format = 1;
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.x = 0;
        mLayoutParams.y = mScreenHeight / 2;
    }

    public void setView(View view) {
        mRootView.removeAllViews();
        mRootView.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void updateMovePosition(int x, int y) {
        if (!this.isShow) {
            return;
        }
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }

        if (x > mScreenWidth) {
            x = mScreenWidth;
        }
        if (y > mScreenHeight) {
            y = mScreenHeight;
        }
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        mWindowManager.updateViewLayout(this.mRootView, mLayoutParams);
    }

    @Override // com.mobile.android.window.IFloatWindow
    public boolean isShowing() {
        return isShow;
    }

    @Override // com.mobile.android.window.IFloatWindow
    public void show() {
        if (!this.isShow) {
            isShow = true;
            mWindowManager.addView(this.mRootView, mLayoutParams);
        }
    }

    @Override // com.mobile.android.window.IFloatWindow
    public void hide() {
        if (this.isShow) {
            isShow = false;
            mWindowManager.removeView(this.mRootView);
        }
    }
}