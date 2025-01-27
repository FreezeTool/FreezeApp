package com.john.freezeapp.hyper;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.aigestudio.wheelpicker.WheelPicker;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.FreezeAppManager;
import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

import java.util.Arrays;
import java.util.List;

public class MiMixFlipAppViewHolder extends CardViewHolder<MiMixFlipAppData> {

    public TextView tvName;
    public ImageView ivIcon;
    public TextView tvOperate;
    public TextView tvForceStop;

    public static MiMixFlipAppViewHolder.Creator<MiMixFlipAppData> CREATOR = new MiMixFlipAppViewHolder.Creator<MiMixFlipAppData>() {
        @Override
        public MiMixFlipAppViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new MiMixFlipAppViewHolder(inflater.inflate(R.layout.item_flip_scale_setting, parent, false));
        }
    };

    public MiMixFlipAppViewHolder(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_image);
        tvName = itemView.findViewById(R.id.tv_name);
        tvOperate = itemView.findViewById(R.id.tv_operate);
        tvForceStop = itemView.findViewById(R.id.tv_force_stop);
    }

    @Override
    public void onBind() {
        super.onBind();

        MiMixFlipAppData data = getData();

        if (data.appModel.icon != null) {
            ivIcon.setImageDrawable(data.appModel.icon);
        }
        if (data.appModel.name != null) {
            tvName.setText(data.appModel.name);
        }
        tvOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAdapter().getListener() instanceof MiMixFlipSettingAdapter.OnItemClickListener) {
                    ((MiMixFlipSettingAdapter.OnItemClickListener) getAdapter().getListener()).scaleSetting(data);
                }
            }

        });

        tvOperate.setText(TextUtils.isEmpty(data.scale) ? getContext().getString(R.string.btn_setting) : data.scale);
        tvForceStop.setVisibility(TextUtils.equals(data.appModel.packageName, BuildConfig.APPLICATION_ID) ? View.GONE : View.VISIBLE);
        tvForceStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAdapter().getListener() instanceof MiMixFlipSettingAdapter.OnItemClickListener) {
                    ((MiMixFlipSettingAdapter.OnItemClickListener) getAdapter().getListener()).forceStop(data);
                }
            }
        });
    }
}
