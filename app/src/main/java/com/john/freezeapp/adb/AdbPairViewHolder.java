package com.john.freezeapp.adb;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class AdbPairViewHolder extends CardViewHolder<AdbPairData> {

    TextView tvTitle, tvSubtitle, tvContent;
    AppCompatButton btnRight, btnLeft;
    ImageView ivIcon;

    public static AdbPairViewHolder.Creator<AdbPairData> CREATOR = new AdbPairViewHolder.Creator<AdbPairData>() {
        @Override
        public AdbPairViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new AdbPairViewHolder(inflater.inflate(R.layout.item_home_freeze_daemon, parent, false));
        }
    };

    public AdbPairViewHolder(View itemView) {
        super(itemView);
        btnRight = itemView.findViewById(R.id.btn_right);
        btnLeft = itemView.findViewById(R.id.btn_left);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        tvSubtitle.setPadding(0, 0, 0, 0);
        tvContent = itemView.findViewById(R.id.tv_content);
        ivIcon = itemView.findViewById(R.id.iv_icon);
    }

    @Override
    public void onBind() {
        super.onBind();
        AdbPairData data = getData();

        tvTitle.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(data.subTitle)) {
            tvSubtitle.setText(data.subTitle);
            tvSubtitle.setVisibility(View.VISIBLE);
        } else {
            tvSubtitle.setText("");
            tvSubtitle.setVisibility(View.GONE);
        }

        tvContent.setVisibility(View.GONE);

        if (data.icon != 0) {
            ivIcon.setImageResource(data.icon);
        }

        AdbPairData.AdbPairBtnData rightBtnData = data.rightBtnData;
        if (rightBtnData != null && rightBtnData.show) {
            btnRight.setText(!TextUtils.isEmpty(rightBtnData.text) ? rightBtnData.text : "");
            btnRight.setOnClickListener(rightBtnData.onClickListener);
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setCompoundDrawablesRelativeWithIntrinsicBounds(getContext().getDrawable(rightBtnData.icon), null, null, null);
        } else {
            btnRight.setVisibility(View.GONE);
        }

    }
}
