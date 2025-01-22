package com.john.freezeapp.battery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class BatteryUsageTitleViewHolder extends CardViewHolder<BatteryUsageTitleData> {

    TextView tvTitle;

    public static CardViewHolder.Creator<BatteryUsageTitleData> CREATOR = new CardViewHolder.Creator<BatteryUsageTitleData>() {
        @Override
        public BatteryUsageTitleViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new BatteryUsageTitleViewHolder(inflater.inflate(R.layout.item_battery_usage_title, parent, false));
        }
    };

    public BatteryUsageTitleViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void onBind() {
        super.onBind();
        BatteryUsageTitleData data = getData();
        tvTitle.setText(data.title);
    }
}
