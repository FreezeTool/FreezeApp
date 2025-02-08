package com.john.freezeapp.setting;

import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import com.john.freezeapp.R;
import com.john.freezeapp.ToolbarActivity;
import com.john.freezeapp.appops.AppOps;
import com.john.freezeapp.appops.AppOpsAdapter;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ScreenUtils;
import com.john.freezeapp.util.SettingUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends ToolbarActivity {

    TextView tvNightMode;
    LinearLayout llNightMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tvNightMode = findViewById(R.id.tv_night_mode);
        llNightMode = findViewById(R.id.ll_night_mode);
        llNightMode.setOnClickListener(v -> showPopupNightModeWindow());
        updateNightMode();
    }

    private void updateNightMode() {
        tvNightMode.setText(getNightName(SettingUtil.getNightMode()));
    }


    private String getNightName(int nightMode) {
        List<Pair<Integer, String>> nightList = getNightList();
        for (Pair<Integer, String> pair : nightList) {
            if (pair.first == nightMode) {
                return pair.second;
            }
        }
        return "";
    }


    private List<Pair<Integer, String>> getNightList() {
        List<Pair<Integer, String>> modes = new ArrayList<>();
        modes.add(new Pair<>(AppCompatDelegate.MODE_NIGHT_NO, "总是关闭"));
        modes.add(new Pair<>(AppCompatDelegate.MODE_NIGHT_YES, "总是开启"));
        modes.add(new Pair<>(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "跟随系统"));
        return modes;
    }


    private void showPopupNightModeWindow() {
        PopupWindow popupWindow = new PopupWindow(getContext());

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        List<Pair<Integer, String>> nightList = getNightList();
        for (Pair<Integer, String> mode : nightList) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_app_ops_popwindow, linearLayout, false);
            TextView tvOperate = itemView.findViewById(R.id.tv_operate);
            tvOperate.setTextColor(getColor(R.color.textColorPrimary));
            tvOperate.setTextSize(16);
            tvOperate.setText(mode.second);
            tvOperate.setOnClickListener(v -> {
                SettingUtil.setNightMode(mode.first);
                updateNightMode();
                AppCompatDelegate.setDefaultNightMode(mode.first);
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
        tvNightMode.getLocationOnScreen(location);
        linearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = linearLayout.getMeasuredWidth();    //  获取测量后的宽度
        int popupHeight = linearLayout.getMeasuredHeight();  //获取测量后的高度

        if (location[1] > ScreenUtils.getScreenHeight(getContext()) / 2) {
            popupWindow.showAtLocation(tvNightMode, Gravity.NO_GRAVITY, ScreenUtils.dp2px(getContext(), 20), location[1] - popupHeight);
        } else {
            popupWindow.showAtLocation(tvNightMode, Gravity.NO_GRAVITY, ScreenUtils.dp2px(getContext(), 20), location[1] + tvNightMode.getMeasuredHeight());
        }
    }


    @Override
    protected String getToolbarTitle() {
        return getString(R.string.setting_name);
    }
}
