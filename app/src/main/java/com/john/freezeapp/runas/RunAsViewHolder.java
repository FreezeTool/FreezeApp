package com.john.freezeapp.runas;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.freezeapp.R;
import com.john.freezeapp.common.CommonAdapter;
import com.john.freezeapp.recyclerview.CardRecyclerViewAdapter;
import com.john.freezeapp.recyclerview.CardViewHolder;
import com.john.freezeapp.util.AppInfoLoader;

public class RunAsViewHolder extends CardViewHolder<RunAsModel> {
    TextView tvName;
    ImageView ivIcon;
    TextView tvRight;

    public static Creator<RunAsModel> CREATOR = (inflater, parent) -> new RunAsViewHolder(inflater.inflate(R.layout.item_run_as, parent, false));

    public RunAsViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        ivIcon = itemView.findViewById(R.id.iv_icon);
        tvRight = itemView.findViewById(R.id.tv_right);
    }

    @Override
    public void onBind() {
        super.onBind();
        RunAsModel data = getData();
        AppInfoLoader.load(getContext(), data.runAsProcessModel.packageName, ivIcon, tvName);
        tvRight.setText(data.runAsProcessModel.active ? R.string.btn_stop_file_server : R.string.btn_start_file_server);
        tvRight.setOnClickListener(v -> {
            Object listener = getAdapter().getListener();
            if (listener instanceof CommonAdapter.ItemListener) {
                ((CommonAdapter.ItemListener) listener).onItemClick(data);
            }
        });
    }
}
