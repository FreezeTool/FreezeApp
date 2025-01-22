package com.john.freezeapp.battery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BatteryUsageSummaryViewHolder extends CardViewHolder<BatteryUsageSummaryData> {
    //public String capacity;
    //    public long startTimestamp;
    //    public long endTimestamp;
    //    public String computedDrain;
    //    public String actualDrain;

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TextView tvTime;
    public TextView tvCapacity;
    public TextView tvComputeDrain;
    public TextView tvActualDrain;


    public static CardViewHolder.Creator<BatteryUsageSummaryData> CREATOR = new CardViewHolder.Creator<BatteryUsageSummaryData>() {
        @Override
        public BatteryUsageSummaryViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new BatteryUsageSummaryViewHolder(inflater.inflate(R.layout.item_battery_usage_summary, parent, false));
        }
    };

    public BatteryUsageSummaryViewHolder(View itemView) {
        super(itemView);
        tvTime = itemView.findViewById(R.id.tv_time);
        tvCapacity = itemView.findViewById(R.id.tv_capacity);
        tvComputeDrain = itemView.findViewById(R.id.tv_computed_drain);
        tvActualDrain = itemView.findViewById(R.id.tv_actual_drain);
    }

    @Override
    public void onBind() {
        super.onBind();
        BatteryUsageSummaryData data = getData();

        String startTime = sDateFormat.format(new Date(data.startTimestamp));
        String endTime = sDateFormat.format(new Date(data.endTimestamp));

        tvTime.setText(startTime + " - " + endTime);

        tvCapacity.setText("Capacity : " + data.capacity);
        tvComputeDrain.setText("Computed drain : " + data.computedDrain);
        tvActualDrain.setText("Actual drain : " + data.actualDrain);

    }
}
