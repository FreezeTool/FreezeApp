package com.john.freezeapp.appops;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class AppOpsDetailViewHolder extends CardViewHolder<AppOpsDetailData> {

    TextView tvName, tvSubName, tvOperate, tvLastAccessTime, tvLastRejectTime, tvRunning;
    LinearLayout llOperate;

    public static Creator<AppOpsDetailData> CREATOR = new Creator<AppOpsDetailData>() {
        @Override
        public AppOpsDetailViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new AppOpsDetailViewHolder(inflater.inflate(R.layout.item_app_ops_detail, parent, false));
        }
    };

    public AppOpsDetailViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        tvSubName = itemView.findViewById(R.id.tv_sub_name);
        tvOperate = itemView.findViewById(R.id.tv_operate);
        llOperate = itemView.findViewById(R.id.ll_operate);
        tvLastAccessTime = itemView.findViewById(R.id.tv_last_access_time);
        tvLastRejectTime = itemView.findViewById(R.id.tv_last_reject_time);
        tvRunning = itemView.findViewById(R.id.tv_running);
    }

    private static final List<Integer> mModes = new ArrayList<>();

    static {
        mModes.add(AppOps.MODE_ALLOWED);
        mModes.add(AppOps.MODE_ERRORED);
        if (FreezeUtil.atLeast29()) {
            mModes.add(AppOps.MODE_FOREGROUND);
        }
    }


    private void showPopupWindow(View view, AppOpsDetailData data) {
        PopupWindow popupWindow = new PopupWindow(getContext());

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (Integer mode : mModes) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_app_ops_popwindow, linearLayout, false);
            TextView tvOperate = itemView.findViewById(R.id.tv_operate);
            tvOperate.setText(AppOps.getModelStr(mode));
            tvOperate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppOps.setUidMode(data.op, mode, data.packageName);
                    if (AppOps.checkOperation(data.op, data.packageName) == mode) {
                        data.mode = mode;
                        getAdapter().notifyDataSetChanged();
                    }
                    popupWindow.dismiss();
                }
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
        view.getLocationOnScreen(location);
        linearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = linearLayout.getMeasuredWidth();    //  获取测量后的宽度
        int popupHeight = linearLayout.getMeasuredHeight();  //获取测量后的高度

        if (location[1] > ScreenUtils.getScreenHeight(getContext()) / 2) {

            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getMeasuredWidth() / 2) - popupWidth/2, location[1] - popupHeight);
        } else {
            popupWindow.showAtLocation(view,  Gravity.NO_GRAVITY, (location[0] + view.getMeasuredWidth() / 2) - popupWidth/2, location[1] + view.getMeasuredHeight());
        }


    }

    @Override
    public void onBind() {
        super.onBind();
        AppOpsDetailData data = getData();
        String opName = AppOps.getOpName(data.op);
        tvName.setText(AppOps.getOpNameStr(getContext(), opName));

        tvSubName.setVisibility(View.GONE);

        if (data.op != data.opSwitchCode) {
            String opSwitchName = AppOps.getOpName(data.opSwitchCode);
            if (opSwitchName != null) {
                tvSubName.setVisibility(View.VISIBLE);
                tvSubName.setText(String.format("由[%s]控制", AppOps.getOpNameStr(getContext(), opSwitchName)));
            }
            llOperate.setVisibility(View.GONE);
        } else {
            if (data.mode != AppOps.MODE_UNKNOWN) {
                tvOperate.setText(AppOps.getModelStr(data.mode));
            } else if (data.defaultMode != AppOps.MODE_UNKNOWN) {
                tvOperate.setText(AppOps.getModelStr(data.defaultMode));
            } else {
                tvOperate.setText("尚未设置");
            }
            llOperate.setOnClickListener(v -> showPopupWindow(tvOperate, data));
            llOperate.setVisibility(View.VISIBLE);
        }


        if (data.lastAccessTime != 0) {
            tvLastAccessTime.setVisibility(View.VISIBLE);
            tvLastAccessTime.setText(String.format("%s前曾允许", FreezeUtil.formatTime(data.nowTime - data.lastAccessTime)));
        } else {
            tvLastAccessTime.setVisibility(View.GONE);
        }

        if (data.lastRejectTime != 0) {
            tvLastRejectTime.setVisibility(View.VISIBLE);
            tvLastRejectTime.setText(String.format("%s前曾拒绝", FreezeUtil.formatTime(data.nowTime - data.lastRejectTime)));
        } else {
            tvLastRejectTime.setVisibility(View.GONE);
        }

        if (data.isRunning) {
            tvRunning.setText("当前权限正在使用中");
            tvRunning.setVisibility(View.VISIBLE);
        } else {
            tvRunning.setVisibility(View.GONE);
        }

    }
}
