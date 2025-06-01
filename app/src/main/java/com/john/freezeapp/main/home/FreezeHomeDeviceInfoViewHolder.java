package com.john.freezeapp.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeDeviceInfoViewHolder extends CardViewHolder<FreezeHomeDeviceInfoData> {

    TextView tvType;
    TextView tvContent;

    public static Creator<FreezeHomeDeviceInfoData> CREATOR = new Creator<FreezeHomeDeviceInfoData>() {
        @Override
        public FreezeHomeDeviceInfoViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeDeviceInfoViewHolder(inflater.inflate(R.layout.item_home_device_info, parent, false));
        }
    };

    public FreezeHomeDeviceInfoViewHolder(View itemView) {
        super(itemView);
        tvType = itemView.findViewById(R.id.tv_type);
        tvContent = itemView.findViewById(R.id.tv_content);
    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeDeviceInfoData data = getData();
        tvType.setText(data.type);
        tvContent.setText(data.content);
    }
}
