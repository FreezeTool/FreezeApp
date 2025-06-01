package com.john.freezeapp.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.R;
import com.john.freezeapp.main.FreezeMainAdapter;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class FreezeHomeDeviceViewHolder extends CardViewHolder<FreezeHomeDeviceData> {

    RecyclerView recyclerView;

    FreezeMainAdapter mAdapter = new FreezeMainAdapter();


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
