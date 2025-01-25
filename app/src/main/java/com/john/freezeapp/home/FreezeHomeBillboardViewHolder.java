package com.john.freezeapp.home;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeBillboardViewHolder extends CardViewHolder<FreezeHomeBillboardData> {

    TextView tvTitle, tvSubtitle, tvContent;
    AppCompatButton btnStart;
    ImageView ivIcon;

    public static Creator<FreezeHomeBillboardData> CREATOR = new Creator<FreezeHomeBillboardData>() {
        @Override
        public FreezeHomeBillboardViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeBillboardViewHolder(inflater.inflate(R.layout.item_home_freeze_daemon, parent, false));
        }
    };

    public FreezeHomeBillboardViewHolder(View itemView) {
        super(itemView);
        btnStart = itemView.findViewById(R.id.btn_start);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        tvContent = itemView.findViewById(R.id.tv_content);

    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeBillboardData data = getData();
        btnStart.setVisibility(View.GONE);
        tvContent.setVisibility(View.GONE);
        if (!data.isActive) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.main_app_server_not_active);
            ivIcon.setImageResource(R.mipmap.ic_help);
            tvSubtitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.main_app_server_active);
            tvSubtitle.setText(String.format("版本 %s , adb", data.version));
            tvSubtitle.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(R.mipmap.ic_success);
            if (!TextUtils.isEmpty(data.tip)) {
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(data.tip);
            }
            if (!TextUtils.isEmpty(data.btn)) {
                btnStart.setText(data.btn);
                btnStart.setVisibility(View.VISIBLE);
                btnStart.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_vector_start), null, null, null);
            }
        }
        btnStart.setOnClickListener(data.onClickStartDaemon);
    }
}
