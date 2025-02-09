package com.john.freezeapp.window;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.john.freezeapp.App;
public class FloatContainer extends FrameLayout {
    private static final int OFFSET_MOVE_X = 10;
    private static final int OFFSET_MOVE_Y = 10;
    private float touchX;
    private float touchY;
    private float rawX;
    private float rawY;
    private float moveX;
    private float moveY;
    private int statusBarHeight;
    private boolean isLongClick = false;
    Handler handler = new Handler(Looper.getMainLooper());

    public FloatContainer(@NonNull Context context) {
        super(context);
    }

    public interface OnFloatListener {
        void onStart();

        void onMove(float x, float y);

        void onEnd();

        void onLongClick();

        void onClick();
    }

    OnFloatListener onFloatListener;

    public void setOnFloatListener(OnFloatListener onFloatListener) {
        this.onFloatListener = onFloatListener;
    }

    private Runnable longClickRunnable = new Runnable() {
        @Override
        public void run() {
            isLongClick = true;
            if (onFloatListener != null) {
                onFloatListener.onLongClick();
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = 0;
                moveY = 0;
                isLongClick = false;
                touchX = event.getX();
                touchY = event.getY();
                rawX = event.getRawX();
                rawY = event.getRawY();
                if (onFloatListener != null) {
                    onFloatListener.onStart();
                }
                handler.removeCallbacks(longClickRunnable);
                handler.postDelayed(longClickRunnable, 1000);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getRawX() - rawX;
                moveY = event.getRawY() - rawY;

                if (moveX > OFFSET_MOVE_X || moveY > OFFSET_MOVE_Y) {
                    handler.removeCallbacks(longClickRunnable);
                }

                if (!isLongClick && onFloatListener != null) {
                    onFloatListener.onMove(event.getRawX() - touchX, event.getRawY() - getStatusBarHeight() - touchY);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                moveX = event.getRawX() - rawX;
                moveY = event.getRawY() - rawY;

                handler.removeCallbacks(longClickRunnable);
                if (onFloatListener != null) {
                    onFloatListener.onEnd();
                }
                if (!isLongClick) {
                    if (moveX < OFFSET_MOVE_X && moveY < OFFSET_MOVE_Y) {
                        if (onFloatListener != null) {
                            onFloatListener.onClick();
                        }
                    }
                }

                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private int getStatusBarHeight() {
        if (this.statusBarHeight == 0) {
            try {
                statusBarHeight = App.getApp().getResources().getDimensionPixelSize(com.android.internal.R.dimen.status_bar_height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
