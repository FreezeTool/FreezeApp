package com.john.freezeapp.window;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.john.freezeapp.App;
import com.john.freezeapp.R;

import java.lang.reflect.Field;

public class FloatWindow implements IFloatWindow {
    private WindowManager.LayoutParams mLayoutParams;
    private View mRootView;
    private float mRootViewX;
    private float mRootViewY;
    private int mScreenHeight;
    private int mScreenWidth;
    private float mTouchInViewX;
    private float mTouchInViewY;
    private WindowManager mWindowManager;
    int statusBarHeight;
    private boolean isShow = false;

    public FloatWindow() {
        mRootView = LayoutInflater.from(App.getApp()).inflate(R.layout.wm_float_view, null);
        mRootView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchInViewX = event.getX();
                    mTouchInViewY = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                case MotionEvent.ACTION_MOVE:
                    mRootViewX = event.getRawX() - mTouchInViewX;
                    mRootViewY = (event.getRawY() - getStatusBarHeight()) - mTouchInViewY;
                    updateMovePosition();
                    return true;
                default:
                    return true;
            }
        });
        mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                hide();
                return true;
            }
        });
        DisplayMetrics displayMetrics = App.getApp().getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mWindowManager = (WindowManager) App.getApp().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        mLayoutParams = layoutParams;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mLayoutParams.format = 1;
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mRootViewY = mScreenHeight / 2;
        mRootViewX = mScreenWidth;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.x = (int) mRootViewX;
        mLayoutParams.y = (int) mRootViewY;
    }

    public void updateMovePosition() {
        if (!this.isShow) {
            return;
        }
        if (this.mRootViewX < 0.0f) {
            mRootViewX = 0.0f;
        }
        if (this.mRootViewY < 0.0f) {
            mRootViewY = 0.0f;
        }

        if (mRootViewX > mScreenWidth) {
            mRootViewX = mScreenWidth;
        }
        if (mRootViewY > mScreenHeight) {
            mRootViewY = mScreenHeight;
        }
        mLayoutParams.x = (int) mRootViewX;
        mLayoutParams.y = (int) mRootViewY;
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

    /* JADX INFO: Access modifiers changed from: private */
    public int getStatusBarHeight() {
        if (this.statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = ((Integer) field.get(o)).intValue();
                statusBarHeight = App.getApp().getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}