package com.john.freezeapp.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.ScreenUtils;

public class FreezeHomeDeviceViewHolder extends CardViewHolder<FreezeHomeDeviceData> {

    RecyclerView recyclerView;

    FreezeHomeAdapter mAdapter = new FreezeHomeAdapter();


    public static Creator<FreezeHomeDeviceData> CREATOR = new Creator<FreezeHomeDeviceData>() {
        @Override
        public FreezeHomeDeviceViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new FreezeHomeDeviceViewHolder(inflater.inflate(R.layout.item_home_device_container, parent, false));
        }
    };

    public FreezeHomeDeviceViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
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
        mAdapter.updateData(data.deviceInfos);
    }

}
