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

public class FreezeHomeBillboardViewHolder extends CardViewHolder<FreezeHomeBillboardData> {

    TextView tvTitle, tvSubtitle, tvContent, tvRight;
    AppCompatButton btnRight, btnLeft;
    ImageView ivIcon;

    public static Creator<FreezeHomeBillboardData> CREATOR = new Creator<FreezeHomeBillboardData>() {
        @Override
        public FreezeHomeBillboardViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeBillboardViewHolder(inflater.inflate(R.layout.item_home_freeze_daemon, parent, false));
        }
    };

    public FreezeHomeBillboardViewHolder(View itemView) {
        super(itemView);
        btnRight = itemView.findViewById(R.id.btn_right);
        btnLeft = itemView.findViewById(R.id.btn_left);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        tvContent = itemView.findViewById(R.id.tv_content);
        tvRight = itemView.findViewById(R.id.tv_right);

    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeBillboardData data = getData();
        btnRight.setVisibility(View.GONE);
        btnLeft.setVisibility(View.GONE);
        tvContent.setVisibility(View.GONE);
        if (!data.isActive) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.main_app_server_not_active);
            ivIcon.setImageResource(R.drawable.ic_vector_help);
            tvSubtitle.setVisibility(View.GONE);
            tvRight.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.main_app_server_active);
            tvSubtitle.setText(data.version);
            tvSubtitle.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(R.drawable.ic_vector_check);
            if (!TextUtils.isEmpty(data.tip)) {
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(data.tip);
            }
            if (!TextUtils.isEmpty(data.btn)) {
                btnRight.setText(data.btn);
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(R.drawable.ic_vector_start), null, null, null);
            }
            if(!TextUtils.isEmpty(data.rightInfo)) {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText(data.rightInfo);
            }
        }
        btnRight.setOnClickListener(data.onClickStartDaemon);
    }
}
