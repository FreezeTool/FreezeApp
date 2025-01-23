package com.john.freezeapp.home;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.ScreenUtils;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeDeviceViewHolder extends CardViewHolder<FreezeHomeDeviceData> {

    LinearLayout llDevice;

    public static Creator<FreezeHomeDeviceData> CREATOR = new Creator<FreezeHomeDeviceData>() {
        @Override
        public FreezeHomeDeviceViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeDeviceViewHolder(inflater.inflate(R.layout.item_home_device_info, parent, false));
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
     * android:layout_marginTop="5dp"
     * android:text=""
     * android:textColor="#666666"
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
                TextView textView = new TextView(getContext());
                textView.setTextColor(0xff666666);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                textView.setText(getDeviceInfo(deviceInfo.type, deviceInfo.content));
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i != 0) {
                    marginLayoutParams.topMargin = ScreenUtils.dp2px(getContext(), 5);
                }
                linearLayout.addView(textView, marginLayoutParams);
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


    private Spannable getDeviceInfo(String type, String content) {
        Spannable spannable = new SpannableString(type + "    " + content);
        spannable.setSpan(new ForegroundColorSpan(0xff555555), 0, type.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}
