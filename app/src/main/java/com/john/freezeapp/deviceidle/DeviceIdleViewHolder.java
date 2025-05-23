package com.john.freezeapp.deviceidle;

import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.AppInfoLoader;

public class DeviceIdleViewHolder extends CardViewHolder<DeviceIdleData> {
    TextView tvName;
    ImageView ivIcon;
    TextView tvRight;


    public static Creator<DeviceIdleData> CREATOR = (inflater, parent) -> new DeviceIdleViewHolder(inflater.inflate(R.layout.item_device_idle, parent, false));

    public DeviceIdleViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        tvRight = itemView.findViewById(R.id.tv_right);


    }

    @Override
    public void onBind() {
        super.onBind();
        DeviceIdleData data = getData();
        AppInfoLoader.load(getContext(), data.packageName, ivIcon, tvName);
        tvRight.setText(data.whiteList == DeviceIdleData.DEVICE_IDLE_WHITE_LIST ? "白名单" : "非白名单");
        tvRight.setOnClickListener(v -> {
            showDialog(data);
        });
    }


    private void showDialog(DeviceIdleData data) {
        new AlertDialog.Builder(getContext())
                .setMessage(data.whiteList == DeviceIdleData.DEVICE_IDLE_WHITE_LIST ? "是否取消白名单?" : "是否添加白名单?")
                .setPositiveButton(R.string.btn_submit, (dialog, which) -> {
                    if (data.whiteList == DeviceIdleData.DEVICE_IDLE_WHITE_LIST) {
                        DeviceIdle.removePowerSaveWhitelistApp(data.packageName);
                    } else {
                        DeviceIdle.addPowerSaveWhitelistApp(data.packageName);
                    }
                    data.whiteList = DeviceIdle.isPowerWhitelist(data.packageName) ? DeviceIdleData.DEVICE_IDLE_WHITE_LIST : DeviceIdleData.DEVICE_IDLE_NOT_WHITE_LIST;
                    onBind();
                })
                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> {

                })
                .show();
    }


}
