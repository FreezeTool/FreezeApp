package com.john.freezeapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.john.freezeapp.client.ClientBinderManager;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseActivity extends AppCompatActivity {

    private IDaemonBinderContainer daemonBinderContainer;
    private ViewGroup mContentView;
    private RelativeLayout mLoadingView;
    private AtomicInteger mLoadingInteger = new AtomicInteger(0);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daemonBinderContainer = ClientBinderManager.getDaemonBinderContainer();
        initBinderContainerListener();
        mContentView = getWindow().getDecorView().findViewById(android.R.id.content);
    }


    protected ViewGroup getLoadingContainer() {
        return mContentView;
    }

    protected void showLoading() {
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


    protected boolean isDaemonActive() {
        return ClientBinderManager.isActive();
    }

    protected void bindDaemon(IDaemonBinderContainer daemonBinderContainer) {

    }

    protected void unbindDaemon() {

    }

    public IDaemonBinderContainer getDaemonBinderContainer() {
        return daemonBinderContainer;
    }

    private void toBindDaemon(IDaemonBinderContainer daemonBinderContainer) {
        this.daemonBinderContainer = daemonBinderContainer;
        bindDaemon(daemonBinderContainer);
    }

    private void toUnbindDaemon() {
        unbindDaemon();
    }


    private void initBinderContainerListener() {
        ClientBinderManager.registerDaemonBinderContainerListener(new ClientBinderManager.IDaemonBinderContainerListener() {
            @Override
            public void bind(IDaemonBinderContainer daemonBinderContainer) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toBindDaemon(daemonBinderContainer);
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
        });
    }

}
