package com.john.freezeapp.home;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.R;
import com.john.freezeapp.ScreenUtils;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeDaemonViewHolder extends CardViewHolder<FreezeHomeDaemonData> {

    TextView tvServer;
    AppCompatButton btnStartServer;

    public static Creator<FreezeHomeDaemonData> CREATOR = new Creator<FreezeHomeDaemonData>() {
        @Override
        public FreezeHomeDaemonViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeDaemonViewHolder(inflater.inflate(R.layout.item_home_freeze_daemon, parent, false));
        }
    };

    public FreezeHomeDaemonViewHolder(View itemView) {
        super(itemView);
        btnStartServer = itemView.findViewById(R.id.btn_start_server);
        tvServer = itemView.findViewById(R.id.tv_server);

    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeDaemonData data = getData();
        if (!data.isActive) {
            tvServer.setVisibility(View.VISIBLE);
            tvServer.setText(R.string.main_app_server_not_active);
            btnStartServer.setVisibility(View.VISIBLE);
            btnStartServer.setOnClickListener(data.onClickStartDaemon);
        } else {
            tvServer.setVisibility(View.VISIBLE);
            String api = String.format("版本 %s , adb", BuildConfig.VERSION_NAME);
            String name = getContext().getResources().getString(R.string.main_app_server_active) + "\n" + api;
            Spannable spannable = new SpannableString(name);
            int start = name.indexOf(api);
            int end = name.length();
            spannable.setSpan(new AbsoluteSizeSpan(ScreenUtils.dp2px(getContext(), 15)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(0xff666666), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvServer.setText(spannable);
            btnStartServer.setVisibility(View.GONE);
        }
    }
}
