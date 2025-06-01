package com.john.freezeapp.main.tool;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.main.tool.data.FreezeHomeToolGroupData;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeToolGroupViewHolder extends CardViewHolder<FreezeHomeToolGroupData> {

    TextView tvTitle;

    public static Creator<FreezeHomeToolGroupData> CREATOR = new Creator<FreezeHomeToolGroupData>() {
        @Override
        public FreezeHomeToolGroupViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeToolGroupViewHolder(inflater.inflate(R.layout.item_home_func_group, parent, false));
        }
    };

    public FreezeHomeToolGroupViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeToolGroupData data = getData();
        tvTitle.setText(data.text);
    }


    public Drawable getBackgroundDrawable(int color) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }
}
