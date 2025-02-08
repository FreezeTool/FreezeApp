package com.john.freezeapp.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.ScreenUtils;

public class FreezeHomeDeviceViewHolder extends CardViewHolder<FreezeHomeDeviceData> {

    LinearLayout llDevice;

    public static Creator<FreezeHomeDeviceData> CREATOR = new Creator<FreezeHomeDeviceData>() {
        @Override
        public FreezeHomeDeviceViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeDeviceViewHolder(inflater.inflate(R.layout.item_home_device_container, parent, false));
        }
    };

    public FreezeHomeDeviceViewHolder(View itemView) {
        super(itemView);
        llDevice = itemView.findViewById(R.id.ll_device);
    }

    /**
     * <TextView
     * android:id="@+id/tv_device_version"
     * android:layout_width="match_parent"
     * android:layout_height="wrap_content"
     * android:text=""
     *
     * android:textSize="15dp" />
     */
    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeDeviceData data = getData();

        if (data.cacheView == null) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < data.deviceInfos.size(); i++) {
                FreezeHomeDeviceData.DeviceInfo deviceInfo = data.deviceInfos.get(i);
                View deviceView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_device_info, linearLayout, false);
                TextView tvType = deviceView.findViewById(R.id.tv_type);
                tvType.setText(deviceInfo.type);
                TextView tvContent = deviceView.findViewById(R.id.tv_content);
                tvContent.setText(deviceInfo.content);
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i != 0) {
                    marginLayoutParams.topMargin = ScreenUtils.dp2px(getContext(), 12);
                }
                linearLayout.addView(deviceView, marginLayoutParams);
            }
            data.cacheView = linearLayout;
        }

        llDevice.removeAllViews();
        ViewParent parent = data.cacheView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeAllViews();
        }
        llDevice.addView(data.cacheView);

    }

}
