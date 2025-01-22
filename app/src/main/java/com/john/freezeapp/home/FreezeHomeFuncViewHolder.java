package com.john.freezeapp.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeFuncViewHolder extends CardViewHolder<FreezeHomeFuncData>{

    Button button;

    public static FreezeHomeFuncViewHolder.Creator<FreezeHomeFuncData> CREATOR = new FreezeHomeFuncViewHolder.Creator<FreezeHomeFuncData>() {
        @Override
        public FreezeHomeFuncViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeFuncViewHolder(inflater.inflate(R.layout.item_home_func, parent, false));
        }
    };

    public FreezeHomeFuncViewHolder(View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.btn_func);
    }

    @Override
    public void onBind() {
        super.onBind();
        FreezeHomeFuncData data = getData();
        button.setText(data.text);
        button.setOnClickListener(data.clickListener);
    }
}
