package com.john.freezeapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.john.freezeapp.client.ClientBinderManager;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseActivity extends AppCompatActivity {

    private IDaemonBinder daemonBinder;
    private ViewGroup mContentView;
    private RelativeLayout mLoadingView;
    private final AtomicInteger mLoadingInteger = new AtomicInteger(0);

    private boolean isDestroy = false;

    ClientBinderManager.IDaemonBinderListener iDaemonBinderListener = new ClientBinderManager.IDaemonBinderListener() {
        @Override
        public void bind(IDaemonBinder daemonBinder) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toBindDaemon(daemonBinder);
                }
            });
        }

        @Override
        public void unbind() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toUnbindDaemon();
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        daemonBinder = ClientBinderManager.getDaemonBinder();
        ClientBinderManager.registerDaemonBinderListener(iDaemonBinderListener);
        mContentView = getWindow().getDecorView().findViewById(android.R.id.content);
    }


    protected ViewGroup getLoadingContainer() {
        return mContentView;
    }

    protected void showLoading() {
        if (isDestroy()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingView == null) {
                    mLoadingView = new RelativeLayout(BaseActivity.this);
                    mLoadingView.setBackgroundColor(0x50000000);
                    mLoadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    ProgressBar progressBar = new ProgressBar(BaseActivity.this);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mLoadingView.addView(progressBar, layoutParams);
                }
                mLoadingInteger.getAndIncrement();
                if (mLoadingView.getParent() == null) {
                    getLoadingContainer().addView(mLoadingView);
                }
            }
        });

    }

    protected void hideLoading() {
        if (isDestroy()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingInteger.get() > 0) {
                    mLoadingInteger.getAndDecrement();
                }
                if (mLoadingInteger.get() == 0 && mLoadingView != null && mLoadingView.getParent() != null) {
                    getLoadingContainer().removeView(mLoadingView);
                }
            }
        });
    }

    protected void forceHideLoading() {
        if (isDestroy()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingInteger.set(0);
                if (mLoadingView != null && mLoadingView.getParent() != null) {
                    getLoadingContainer().removeView(mLoadingView);
                }
            }
        });
    }

    protected boolean isDestroy() {
        return isDestroy;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientBinderManager.unregisterDaemonBinderListener(iDaemonBinderListener);
        removeDelayHideLoading();
        isDestroy = true;
    }

    protected boolean isDaemonActive() {
        return ClientBinderManager.isActive();
    }

    protected void bindDaemon(IDaemonBinder daemonBinder) {

    }

    protected void unbindDaemon() {

    }

    public IDaemonBinder getDaemonBinder() {
        return daemonBinder;
    }

    private void toBindDaemon(IDaemonBinder daemonBinder) {
        this.daemonBinder = daemonBinder;
        bindDaemon(daemonBinder);
        removeDelayHideLoading();
    }

    private void toUnbindDaemon() {
        unbindDaemon();
        removeDelayHideLoading();
    }


    private void initBinderContainerListener() {

    }


    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private final Runnable mHideLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            hideLoading();
        }
    };

    protected void hideLoading(long delay) {
        if (isDestroy()) {
            return;
        }
        mHandler.postDelayed(mHideLoadingRunnable, delay);
    }

    private void removeDelayHideLoading() {
        mHandler.removeCallbacks(mHideLoadingRunnable);
    }

    protected void postUI(Runnable runnable) {
        mHandler.post(runnable);
    }

    protected void postDelayUI(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }


    protected Context getContext() {
        return this;
    }

}
