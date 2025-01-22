package com.john.freezeapp.battery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class BatteryUsageDeviceViewHolder extends CardViewHolder<BatteryUsageDeviceData> {

    public TextView tvDevice;
//    public TextView tvApps;
    public TextView tvDuration;


    public static Creator<BatteryUsageDeviceData> CREATOR = new Creator<BatteryUsageDeviceData>() {
        @Override
        public BatteryUsageDeviceViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new BatteryUsageDeviceViewHolder(inflater.inflate(R.layout.item_battery_usage_device, parent, false));
        }
    };

    public BatteryUsageDeviceViewHolder(View itemView) {
        super(itemView);

        tvDevice = itemView.findViewById(R.id.tv_device);
//        tvApps = itemView.findViewById(R.id.tv_apps);
        tvDuration = itemView.findViewById(R.id.tv_duration);
    }

    @Override
    public void onBind() {
        super.onBind();
        BatteryUsageDeviceData data = getData();
        tvDevice.setText(data.deviceLabel + " : " + data.devicePowerMah + " , " + data.appsLabel + " : " + data.appsPowerMah);
        tvDuration.setText(data.durationLabel + " : " + data.duration);
    }


}
