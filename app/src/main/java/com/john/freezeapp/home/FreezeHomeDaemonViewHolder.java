package com.john.freezeapp.home;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeDaemonViewHolder extends CardViewHolder<FreezeHomeDaemonData> {

    TextView tvTitle, tvSubtitle, tvContent;
    AppCompatButton btnStart;
    ImageView ivIcon;
    public static CardViewHolder.Creator<FreezeHomeDaemonData> CREATOR = new CardViewHolder.Creator<FreezeHomeDaemonData>() {
        @Override
        public FreezeHomeDaemonViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeDaemonViewHolder(inflater.inflate(R.layout.item_home_freeze_daemon, parent, false));
        }
    };

    public FreezeHomeDaemonViewHolder(View itemView) {
        super(itemView);
        btnStart = itemView.findViewById(R.id.btn_start);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        tvContent = itemView.findViewById(R.id.tv_content);
        ivIcon = itemView.findViewById(R.id.iv_icon);

    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeDaemonData data = getData();
        tvTitle.setText(data.title != null ? data.title : "");
        if (!TextUtils.isEmpty(data.content)) {
            tvContent.setText(data.content);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }
        if (data.icon != 0) {
            ivIcon.setImageResource(data.icon);
        }
        btnStart.setText(!TextUtils.isEmpty(data.btnText) ? data.btnText : "");
        btnStart.setOnClickListener(data.onClickListener);
        btnStart.setVisibility(data.showBtn ? View.VISIBLE : View.GONE);
        btnStart.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(data.btnLeftDrawable), null, null, null);
    }
}
