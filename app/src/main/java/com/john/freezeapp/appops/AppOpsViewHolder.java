package com.john.freezeapp.appops;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.AppInfoLoader;

public class AppOpsViewHolder extends CardViewHolder<AppOpsData> {

    public TextView tvName;
    public ImageView ivIcon;

    public static Creator<AppOpsData> CREATOR = new Creator<AppOpsData>() {
        @Override
        public AppOpsViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new AppOpsViewHolder(inflater.inflate(R.layout.item_app_ops, parent, false));
        }
    };

    public AppOpsViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_image);
        tvName = itemView.findViewById(R.id.tv_name);

    }

    @Override
    public void onBind() {
        super.onBind();
        AppOpsData data = getData();
        AppInfoLoader.load(getContext(), data.appModel.packageName, ivIcon, tvName);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AppOpsDetailActivity.class);
                intent.putExtra(AppOpsDetailActivity.KEY_PACKAGE, data.appModel.packageName);
                getContext().startActivity(intent);
            }
        });
    }
}
