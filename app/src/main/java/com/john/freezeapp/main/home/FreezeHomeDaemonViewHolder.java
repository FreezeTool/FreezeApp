package com.john.freezeapp.main.home;

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
    AppCompatButton btnRight, btnLeft;
    ImageView ivIcon;
    public static CardViewHolder.Creator<FreezeHomeDaemonData> CREATOR = new CardViewHolder.Creator<FreezeHomeDaemonData>() {
        @Override
        public FreezeHomeDaemonViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeDaemonViewHolder(inflater.inflate(R.layout.item_home_freeze_daemon, parent, false));
        }
    };

    public FreezeHomeDaemonViewHolder(View itemView) {
        super(itemView);
        btnRight = itemView.findViewById(R.id.btn_right);
        btnLeft = itemView.findViewById(R.id.btn_left);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        tvContent = itemView.findViewById(R.id.tv_content);
        ivIcon = itemView.findViewById(R.id.iv_icon);

    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeDaemonData data = getData();
        if (!TextUtils.isEmpty(data.title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(data.title);
        } else {
            tvTitle.setVisibility(View.GONE);
            tvTitle.setText("");
        }

        if (!TextUtils.isEmpty(data.content)) {
            tvContent.setText(data.content);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }
        if (data.icon != 0) {
            ivIcon.setImageResource(data.icon);
        }

        tvSubtitle.setVisibility(View.GONE);

        FreezeHomeDaemonData.DaemonBtnData rightDaemonBtnData = data.rightDaemonBtnData;
        if (rightDaemonBtnData != null && rightDaemonBtnData.show) {
            btnRight.setText(!TextUtils.isEmpty(rightDaemonBtnData.text) ? rightDaemonBtnData.text : "");
            btnRight.setOnClickListener(rightDaemonBtnData.onClickListener);
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(rightDaemonBtnData.icon), null, null, null);
        } else {
            btnRight.setVisibility(View.GONE);
        }

        FreezeHomeDaemonData.DaemonBtnData leftDaemonBtnData = data.leftDaemonBtnData;
        if (leftDaemonBtnData != null && leftDaemonBtnData.show) {
            btnLeft.setText(!TextUtils.isEmpty(leftDaemonBtnData.text) ? leftDaemonBtnData.text : "");
            btnLeft.setOnClickListener(leftDaemonBtnData.onClickListener);
            btnLeft.setVisibility(View.VISIBLE);
            btnLeft.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(leftDaemonBtnData.icon), null, null, null);
        } else {
            btnLeft.setVisibility(View.GONE);
        }

    }
}
