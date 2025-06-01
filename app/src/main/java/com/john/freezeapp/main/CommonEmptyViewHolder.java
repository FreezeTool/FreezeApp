package com.john.freezeapp.main;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class CommonEmptyViewHolder extends CardViewHolder<CommonEmptyData> {

    LinearLayout llEmptyContainer;
    TextView tvEmptyContent;

    public static Creator<CommonEmptyData> CREATOR = new Creator<CommonEmptyData>() {
        @Override
        public CommonEmptyViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new CommonEmptyViewHolder(inflater.inflate(R.layout.item_common_empty, parent, false));
        }
    };

    public CommonEmptyViewHolder(View itemView) {
        super(itemView);
        llEmptyContainer = itemView.findViewById(R.id.ll_empty_container);
        tvEmptyContent = itemView.findViewById(R.id.tv_empty_content);

    }

    @Override
    public void onBind() {
        super.onBind();
        CommonEmptyData data = getData();
        tvEmptyContent.setText(!TextUtils.isEmpty(data.content) ? data.content : "");
        llEmptyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, data.height));
        tvEmptyContent.setOnClickListener(data.onClickListener);
    }

}
