package com.john.freezeapp.appops;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.AppInfoLoader;
import com.john.freezeapp.util.FreezeUtil;

public class AppOpsPackageDetailViewHolder extends CardViewHolder<AppOpsPackageDetailData> {

    TextView tvName, tvPackage, tvUid, tvApi, tvVersion;
    ImageView ivIcon;

    public static Creator<AppOpsPackageDetailData> CREATOR = (inflater, parent) -> new AppOpsPackageDetailViewHolder(inflater.inflate(R.layout.item_app_ops_package_detail, parent, false));

    public AppOpsPackageDetailViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        tvPackage = itemView.findViewById(R.id.tv_package);
        tvUid = itemView.findViewById(R.id.tv_uid);
        tvApi = itemView.findViewById(R.id.tv_api);
        tvVersion = itemView.findViewById(R.id.tv_version);
        ivIcon = itemView.findViewById(R.id.iv_icon);
    }

    @Override
    public void onBind() {
        super.onBind();
        AppOpsPackageDetailData data = getData();
        AppInfoLoader.load(getContext(), data.packageName, ivIcon, tvName);
        setSafeText(tvUid, String.format("UID %s", String.valueOf(data.uid)), "");
        setSafeText(tvPackage, data.packageName, "");
        setSafeText(tvVersion, String.format("版本 %s", data.versionName), "");
        setSafeText(tvApi, String.format("API %s", String.valueOf(data.api)), "");
    }

    private void setSafeText(TextView textView, String text, String defText) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        } else {
            textView.setText(defText);
        }
    }
}