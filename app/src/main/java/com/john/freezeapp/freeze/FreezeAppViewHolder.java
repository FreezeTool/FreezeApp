package com.john.freezeapp.freeze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.john.freezeapp.FreezeAppManager;
import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeAppViewHolder extends CardViewHolder<FreezeAppData> {

    public TextView tvName;
    public ImageView ivIcon;
    public TextView tvOperate;
    public LinearLayout llProcess;
    public ViewGroup appContainer;

    public static FreezeAppViewHolder.Creator<FreezeAppData> CREATOR = new FreezeAppViewHolder.Creator<FreezeAppData>() {
        @Override
        public FreezeAppViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeAppViewHolder(inflater.inflate(R.layout.item_app, parent, false));
        }
    };

    public FreezeAppViewHolder(View itemView) {
        super(itemView);
        appContainer = itemView.findViewById(R.id.app_container);
        ivIcon = itemView.findViewById(R.id.iv_image);
        tvName = itemView.findViewById(R.id.tv_name);
        tvOperate = itemView.findViewById(R.id.tv_operate);
        llProcess = itemView.findViewById(R.id.process_info);
    }

    @Override
    public void onBind() {
        super.onBind();

        FreezeAppData data = getData();

        if (data.appModel.icon != null) {
            ivIcon.setImageDrawable(data.appModel.icon);
        }
        if (data.appModel.name != null) {
            tvName.setText(data.appModel.name);
        }
        tvOperate.setText(data.rightName);
        tvOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object listener = getAdapter().getListener();
                if (listener instanceof FreezeAppAdapter.OnItemClick) {
                    ((FreezeAppAdapter.OnItemClick) listener).onRightClick(data.appModel);
                }
            }
        });

        if (data.appModel instanceof FreezeAppManager.RunningModel) {
            appContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.isProcessExpand = !data.isProcessExpand;
                    getAdapter().notifyDataSetChanged();
                }
            });
        }

        if (data.appModel instanceof FreezeAppManager.RunningModel) {
            if (data.cacheView == null) {
                Context context = llProcess.getContext();
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                for (FreezeAppManager.ProcessModel processModel : ((FreezeAppManager.RunningModel) data.appModel).processModels) {
                    View processView = LayoutInflater.from(context).inflate(R.layout.process_info, null);
                    TextView processName = processView.findViewById(R.id.tv_process_name);
                    TextView processTime = processView.findViewById(R.id.tv_process_time);
                    processName.setText(processModel.processName);
                    processTime.setText("PID - " + processModel.time);
                    linearLayout.addView(processView);
                }
                data.cacheView = linearLayout;
            }

            llProcess.removeAllViews();
            ViewParent parent = data.cacheView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeAllViews();
            }
            llProcess.removeView(data.cacheView);
            llProcess.setVisibility(View.GONE);

            if (data.isProcessExpand) {
                llProcess.setVisibility(View.VISIBLE);
                llProcess.addView(data.cacheView);
            }
        }
    }
}
