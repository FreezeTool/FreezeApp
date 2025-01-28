package com.john.freezeapp.battery;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.john.freezeapp.AppInfoLoader;
import com.john.freezeapp.FreezeAppManager;
import com.john.freezeapp.R;
import com.john.freezeapp.ScreenUtils;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class BatteryUsageAppViewHolder extends CardViewHolder<BatteryUsageAppData> {

    TextView tvName;
    ImageView ivIcon;
    LinearLayout llHardwareContainer;
    RelativeLayout llAppContainer;

    public static CardViewHolder.Creator<BatteryUsageAppData> CREATOR = new CardViewHolder.Creator<BatteryUsageAppData>() {
        @Override
        public BatteryUsageAppViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new BatteryUsageAppViewHolder(inflater.inflate(R.layout.item_battery_usage_app, parent, false));
        }
    };

    public BatteryUsageAppViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        llHardwareContainer = itemView.findViewById(R.id.ll_hardware_container);
        llAppContainer = itemView.findViewById(R.id.app_container);
    }

    @Override
    public void onBind() {
        super.onBind();
        BatteryUsageAppData data = getData();

        ivIcon.setBackground(getContext().getDrawable(R.mipmap.ic_app_icon));

        tvName.setText("UID - " + data.uid);

        AppInfoLoader.load(getContext(), data.packageName, ivIcon, tvName);


        if (data.cacheView == null) {
            Context context = getContext();
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            for (BatteryUsageAppData.HardwareData hardwareData : data.hardwareDatas) {
                TextView textView = new TextView(context);
                textView.setText(hardwareData.label + "=" + hardwareData.content);
                int left = ScreenUtils.dp2px(context, 20);
                int top = ScreenUtils.dp2px(context, 10);
                linearLayout.setPadding(left, top, left, top);
                linearLayout.addView(textView);
            }
            data.cacheView = linearLayout;
        }
        llHardwareContainer.removeAllViews();
        ViewParent parent = data.cacheView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeAllViews();
        }
        if (data.isExpand) {
            llHardwareContainer.addView(data.cacheView);
        }
        llAppContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.isExpand = !data.isExpand;
                getAdapter().notifyDataSetChanged();
            }
        });
    }

}
