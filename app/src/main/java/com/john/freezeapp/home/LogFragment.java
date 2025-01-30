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
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.R;
import com.john.freezeapp.client.ClientLogBinderManager;
import com.john.freezeapp.recyclerview.CardData;

import java.util.ArrayList;
import java.util.List;

public class LogFragment extends BaseFragment {
    FreezeHomeAdapter homeAdapter = new FreezeHomeAdapter();
    RecyclerView recyclerView;

    ClientLogBinderManager.ClientLogCallback callback = new ClientLogBinderManager.ClientLogCallback() {
        @Override
        public void log(String msg) {
            addMsg(msg);
        }
    };

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
        ClientLogBinderManager.registerClientLogCallback(callback);
        updateData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClientLogBinderManager.unregisterClientLogCallback(callback);
    }

    private void addMsg(String msg) {
        postUI(new Runnable() {
            @Override
            public void run() {
                if (isDaemonActive()) {
                    homeAdapter.addData(new FreezeHomeLogData(msg));
                }
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
        postUI(new Runnable() {
            @Override
            public void run() {
                List<CardData> list = new ArrayList<>();
                if (isDaemonActive()) {
                    for (ClientLogBinderManager.LogData logData : ClientLogBinderManager.getLogData()) {
                        list.add(new FreezeHomeLogData(logData.msg));
                    }
                } else {
                    list.add(new CommonEmptyData(recyclerView.getMeasuredHeight(), getContext().getString(R.string.main_home_empty_content)));
                }
                homeAdapter.updateData(list);
            }
        });
    }
}
