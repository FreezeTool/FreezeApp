package com.john.freezeapp.usagestats.appstandby;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.AppInfoLoader;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ScreenUtils;

import java.util.List;

public class AppStandbyBucketViewHolder extends CardViewHolder<AppStandbyData> {
    TextView tvName;
    ImageView ivIcon;
    LinearLayout llContainer;
    TextView tvAppStandbyBucket;


    public static Creator<AppStandbyData> CREATOR = (inflater, parent) -> new AppStandbyBucketViewHolder(inflater.inflate(R.layout.item_app_standby_bucket, parent, false));

    public AppStandbyBucketViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        llContainer = itemView.findViewById(R.id.ll_container);
        tvAppStandbyBucket = itemView.findViewById(R.id.tv_app_standby_bucket);
    }

    @Override
    public void onBind() {
        super.onBind();
        AppStandbyData data = getData();
        tvAppStandbyBucket.setText(AppStandby.getStandByBucketName(data.standbyBucket));
        itemView.setOnClickListener(v -> {
            if (data.standbyBucket == StandbyBucket.STANDBY_BUCKET_EXEMPTED.getBucket()) {
                FreezeUtil.showShortToast(String.format("无法设置%s应用~", StandbyBucket.STANDBY_BUCKET_EXEMPTED.getName()));
                return;
            }
            showPopupNightModeWindow(data);
        });

        AppInfoLoader.load(getContext(), data.packageName, ivIcon, tvName);

    }

    private void showPopupNightModeWindow(AppStandbyData appStandbyData) {
        PopupWindow popupWindow = new PopupWindow(getContext());

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        List<StandbyBucket> standbyBucket = AppStandby.getStandbyBucket();
        for (StandbyBucket bucket : standbyBucket) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_app_ops_popwindow, linearLayout, false);
            TextView tvOperate = itemView.findViewById(R.id.tv_operate);
            tvOperate.setTextColor(getContext().getColor(R.color.textColorPrimary));
            tvOperate.setText(bucket.getName());
            tvOperate.setOnClickListener(v -> {
                AppStandby.setAppStandbyBucket(appStandbyData.packageName, bucket.getBucket());
                int appStandbyBucket = AppStandby.getAppStandbyBucket(appStandbyData.packageName);
                if (appStandbyBucket != -1) {
                    appStandbyData.standbyBucket = appStandbyBucket;
                    onBind();
                } else {
                    FreezeUtil.showShortToast("设置失败～");
                }
                popupWindow.dismiss();
            });
            linearLayout.addView(itemView, new LinearLayout.LayoutParams(ScreenUtils.dp2px(getContext(), 100), ScreenUtils.dp2px(getContext(), LinearLayout.LayoutParams.WRAP_CONTENT)));

        }
        popupWindow.setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.bg_app_ops_popup_window));
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setContentView(linearLayout);
        int[] location = new int[2];
        tvAppStandbyBucket.getLocationOnScreen(location);
        linearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = linearLayout.getMeasuredWidth();    //  获取测量后的宽度
        int popupHeight = linearLayout.getMeasuredHeight();  //获取测量后的高度

        if (location[1] > ScreenUtils.getScreenHeight(getContext()) / 2) {

            popupWindow.showAtLocation(tvAppStandbyBucket, Gravity.NO_GRAVITY, (location[0] + tvAppStandbyBucket.getMeasuredWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        } else {
            popupWindow.showAtLocation(tvAppStandbyBucket, Gravity.NO_GRAVITY, (location[0] + tvAppStandbyBucket.getMeasuredWidth() / 2) - popupWidth / 2, location[1] + tvAppStandbyBucket.getMeasuredHeight());
        }
    }


}
