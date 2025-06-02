package com.john.freezeapp.storage;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.AppInfoLoader;
import com.john.freezeapp.util.CommonUtil;
import com.john.freezeapp.util.FreezeUtil;

@RequiresApi(Build.VERSION_CODES.O)
public class StorageViewHolder extends CardViewHolder<StorageData> {

    public TextView tvName;
    public ImageView ivIcon;
    public TextView tvCache;

    public static Creator<StorageData> CREATOR = (inflater, parent) -> new StorageViewHolder(inflater.inflate(R.layout.item_storage, parent, false));

    public StorageViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_image);
        tvName = itemView.findViewById(R.id.tv_name);
        tvCache = itemView.findViewById(R.id.tv_cache);

    }

    @Override
    public void onBind() {
        super.onBind();
        StorageData data = getData();
        AppInfoLoader.load(getContext(), data.packageName, ivIcon, tvName);
        if(data.storageStats == null) {
            tvCache.setText("正在计算");
        } else {
            tvCache.setText(CommonUtil.getSizeText(data.cacheBytes) + " 缓存");
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object listener = getAdapter().getListener();
                if(listener instanceof StorageAdapter.OnItemClick) {
                    ((StorageAdapter.OnItemClick) listener).onItemClick(data);
                }
            }
        });
    }
}
