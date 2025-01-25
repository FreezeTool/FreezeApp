package com.john.freezeapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.john.freezeapp.client.ClientBinderManager;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseFragment extends Fragment {
    private IDaemonBinderContainer daemonBinderContainer;
    private ViewGroup mContentView;
    private RelativeLayout mLoadingView;
    private final AtomicInteger mLoadingInteger = new AtomicInteger(0);

    ClientBinderManager.IDaemonBinderContainerListener iDaemonBinderContainerListener = new ClientBinderManager.IDaemonBinderContainerListener() {
        @Override
        public void bind(IDaemonBinderContainer daemonBinderContainer) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    toBindDaemon(daemonBinderContainer);
                }
            });
        }

        @Override
        public void unbind() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    toUnbindDaemon();
                }
            });
        }
    };

    private boolean isDestroy = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        mContentView = getActivity().getWindow().findViewById(android.R.id.content);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        daemonBinderContainer = ClientBinderManager.getDaemonBinderContainer();
        initBinderContainerListener();
    }

    protected ViewGroup getLoadingContainer() {
        return mContentView;
    }

    protected void showLoading() {
        if (isDestroy()) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLoadingView == null) {
                    mLoadingView = new RelativeLayout(getContext());
                    mLoadingView.setBackgroundColor(0x50000000);
                    mLoadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    ProgressBar progressBar = new ProgressBar(getContext());
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
        mHandler.post(new Runnable() {
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
        mHandler.post(new Runnable() {
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
    public void onDestroy() {
        super.onDestroy();
        removeDelayHideLoading();
        ClientBinderManager.unregisterDaemonBinderContainerListener(iDaemonBinderContainerListener);
        isDestroy = true;
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
        removeDelayHideLoading();
    }

    private void toUnbindDaemon() {
        unbindDaemon();
        removeDelayHideLoading();
    }


    private void initBinderContainerListener() {
        ClientBinderManager.registerDaemonBinderContainerListener(iDaemonBinderContainerListener);
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
}
