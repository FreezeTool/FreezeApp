package com.john.freezeapp.home;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeLogViewHolder extends CardViewHolder<FreezeHomeLogData> {

    TextView tvContent;

    public static FreezeHomeLogViewHolder.Creator<FreezeHomeLogData> CREATOR = new FreezeHomeLogViewHolder.Creator<FreezeHomeLogData>() {
        @Override
        public FreezeHomeLogViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeLogViewHolder(inflater.inflate(R.layout.item_home_log, parent, false));
        }
    };

    public FreezeHomeLogViewHolder(View itemView) {
        super(itemView);
        tvContent = itemView.findViewById(R.id.tv_content);

    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeLogData data = getData();
        tvContent.setText(data.msg);
    }
}
