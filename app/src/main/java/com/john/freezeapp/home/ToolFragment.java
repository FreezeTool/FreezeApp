package com.john.freezeapp.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseFragment;
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.MainActivity;
import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardData;

import java.util.ArrayList;
import java.util.List;

public class ToolFragment extends BaseFragment {

    FreezeHomeAdapter homeAdapter = new FreezeHomeAdapter();
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(homeAdapter);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                updateData();
            }
        });
    }

    @Override
    protected void bindDaemon(IDaemonBinder daemonBinder) {
        super.bindDaemon(daemonBinder);
        updateData();
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        updateData();
    }

    private void updateData() {

        if (!isAdded()) {
            return;
        }

        List<CardData> list = new ArrayList<>();
        if (isDaemonActive()) {
            List<FreezeHomeToolData> freezeHomeFuncData = FreezeHomeToolHelper.getFreezeHomeFuncData(getContext());
            if (freezeHomeFuncData != null) {
                list.addAll(freezeHomeFuncData);
            }
        } else {
            CommonEmptyData commonEmptyData = new CommonEmptyData();
            commonEmptyData.type = CommonEmptyData.TYPE_NOT_BIND;
            commonEmptyData.content = getContext().getString(R.string.main_home_daemon_not_active_content);
            commonEmptyData.height = recyclerView.getMeasuredHeight();
            commonEmptyData.onClickListener = v -> switchHomeFragment();
            list.add(commonEmptyData);

        }
        homeAdapter.updateData(list);
    }

    private void switchHomeFragment() {
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).switchHomeFragment();
        }
    }


}
