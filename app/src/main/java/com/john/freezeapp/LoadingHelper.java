package com.john.freezeapp;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

public class LoadingHelper {
    public static RelativeLayout getLoadingView(Context context) {
        RelativeLayout loadingView = new RelativeLayout(context);
        loadingView.setOnClickListener(v -> {
        });
        loadingView.setBackgroundColor(0x30000000);
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ProgressBar progressBar = new ProgressBar(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingView.addView(progressBar, layoutParams);
        return loadingView;
    }

    public static ViewGroup getLoadingContainer(FragmentActivity activity) {
        return (ViewGroup) activity.getWindow().getDecorView();
    }
}
