package com.john.freezeapp.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseFragment;
import com.john.freezeapp.IDaemonBinderContainer;
import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardData;

import java.util.ArrayList;
import java.util.List;

public class FuncFragment extends BaseFragment {

    FreezeHomeAdapter homeAdapter = new FreezeHomeAdapter();
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_func, container, false);
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
    protected void bindDaemon(IDaemonBinderContainer daemonBinderContainer) {
        super.bindDaemon(daemonBinderContainer);
        updateData();
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        updateData();
    }

    private void updateData() {
        List<CardData> list = new ArrayList<>();
        if (isDaemonActive()) {
            List<FreezeHomeFuncData> freezeHomeFuncData = FreezeHomeFuncHelper.getFreezeHomeFuncData(getContext());
            if (freezeHomeFuncData != null) {
                list.addAll(freezeHomeFuncData);
            }
        } else {

            list.add(new CommonEmptyData(recyclerView.getMeasuredHeight(), "启动FreezeApp服务手使用～"));
        }
        homeAdapter.updateData(list);
    }


}
