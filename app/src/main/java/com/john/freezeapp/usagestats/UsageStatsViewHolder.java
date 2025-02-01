package com.john.freezeapp.usagestats;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.john.freezeapp.util.AppInfoLoader;
import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.FreezeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UsageStatsViewHolder extends CardViewHolder<UsageStatsData> {
    TextView tvName;
    ImageView ivIcon;
    LinearLayout llContainer;
    TextView tvFirstTimeStamp, tvTotalTimeVisible, tvLaunchCount, tvAppLaunchCount;
    RelativeLayout rlAppContainer;

    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Creator<UsageStatsData> CREATOR = new Creator<UsageStatsData>() {
        @Override
        public UsageStatsViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new UsageStatsViewHolder(inflater.inflate(R.layout.item_usage_stats, parent, false));
        }
    };

    public UsageStatsViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        llContainer = itemView.findViewById(R.id.ll_container);
        tvAppLaunchCount = itemView.findViewById(R.id.tv_app_launch_count);
        tvLaunchCount = itemView.findViewById(R.id.tv_launch_count);
        tvTotalTimeVisible = itemView.findViewById(R.id.tv_total_time_visible);
        tvFirstTimeStamp = itemView.findViewById(R.id.tv_first_time_stamp);
        rlAppContainer = itemView.findViewById(R.id.app_container);
    }

    @Override
    public void onBind() {
        super.onBind();
        UsageStatsData data = getData();
        tvTotalTimeVisible.setText(FreezeUtil.formatTime(data.totalTimeVisible));
        tvFirstTimeStamp.setText(sDateFormat.format(new Date(data.firstTimeStamp)));
        tvAppLaunchCount.setText(String.format("%s次", String.valueOf(data.appLaunchCount)));
        tvLaunchCount.setText(String.format("%s次", String.valueOf(data.launchCount)));
        llContainer.setVisibility(data.isExpand ? View.VISIBLE : View.GONE);
        rlAppContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.isExpand = !data.isExpand;
                getAdapter().notifyDataSetChanged();
            }
        });

        AppInfoLoader.load(getContext(), data.packageName, ivIcon, tvName);

    }
}
