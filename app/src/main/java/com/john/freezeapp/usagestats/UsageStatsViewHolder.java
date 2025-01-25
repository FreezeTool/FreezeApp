package com.john.freezeapp.usagestats;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.home.FreezeHomeFuncData;
import com.john.freezeapp.recyclerview.CardViewHolder;

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
        ivIcon.setBackground(data.icon != null ? data.icon : getContext().getDrawable(R.mipmap.ic_app_icon));
        tvName.setText(!TextUtils.isEmpty(data.name) ? data.name : (!TextUtils.isEmpty(data.packageName) ? data.packageName : ""));
        tvTotalTimeVisible.setText(formatTime(data.totalTimeVisible));
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

    }


    private String formatTime(long time) {
        StringBuilder stringBuilder = new StringBuilder();

        if (time > DateUtils.DAY_IN_MILLIS) {
            int day = (int) (time / DateUtils.DAY_IN_MILLIS);
            stringBuilder.append(day).append("天");
            time = time % DateUtils.DAY_IN_MILLIS;
        }

        if (time > DateUtils.HOUR_IN_MILLIS) {
            int hour = (int) (time / DateUtils.HOUR_IN_MILLIS);
            stringBuilder.append(hour).append("小时");
            time = time % DateUtils.HOUR_IN_MILLIS;
        }

        if (time > DateUtils.MINUTE_IN_MILLIS) {
            int minute = (int) (time / DateUtils.MINUTE_IN_MILLIS);
            stringBuilder.append(minute).append("分");
            time = time % DateUtils.MINUTE_IN_MILLIS;
        }

        if (time > DateUtils.SECOND_IN_MILLIS) {
            int second = (int) (time / DateUtils.SECOND_IN_MILLIS);
            stringBuilder.append(second).append("秒");
            time = time % DateUtils.SECOND_IN_MILLIS;
        }

        if (stringBuilder.toString().isEmpty()) {
            stringBuilder.append(time).append("毫秒");
        }
        return stringBuilder.toString();
    }
}
