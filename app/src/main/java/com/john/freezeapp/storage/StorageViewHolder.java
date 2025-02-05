package com.john.freezeapp.storage;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.appops.AppOpsDetailActivity;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.AppInfoLoader;

public class StorageViewHolder extends CardViewHolder<StorageData> {

    public TextView tvName;
    public ImageView ivIcon;

    public static Creator<StorageData> CREATOR = (inflater, parent) -> new StorageViewHolder(inflater.inflate(R.layout.item_storage, parent, false));

    public StorageViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_image);
        tvName = itemView.findViewById(R.id.tv_name);

    }

    @Override
    public void onBind() {
        super.onBind();
        StorageData data = getData();
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
