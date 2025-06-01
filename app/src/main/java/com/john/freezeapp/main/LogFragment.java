package com.john.freezeapp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.john.freezeapp.BaseFragment;
import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.R;
import com.john.freezeapp.client.ClientLogBinderManager;
import com.john.freezeapp.main.home.FreezeHomeLogData;
import com.john.freezeapp.recyclerview.CardData;

import java.util.ArrayList;
import java.util.List;

public class LogFragment extends BaseFragment {
    FreezeMainAdapter homeAdapter = new FreezeMainAdapter();
    RecyclerView recyclerView;

    ClientLogBinderManager.ClientLogCallback callback = new ClientLogBinderManager.ClientLogCallback() {
        @Override
        public void log(String msg) {
            addMsg(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_log_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_clear_log) {
            ClientLogBinderManager.clearLog();
            updateData();

        }
        return super.onOptionsItemSelected(item);
    }

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

                    List<Object> items = homeAdapter.getItems();
                    if (items.size() == 1) {
                        Object object = items.get(0);
                        if (object instanceof CommonEmptyData) {
                            items.clear();
                        }
                    }

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

                if (!isDaemonActive()) {
                    CommonEmptyData commonEmptyData = new CommonEmptyData();
                    commonEmptyData.type = CommonEmptyData.TYPE_NOT_BIND;
                    commonEmptyData.content = getContext().getString(R.string.main_home_daemon_not_active_content);
                    commonEmptyData.height = recyclerView.getMeasuredHeight();
                    commonEmptyData.onClickListener = v -> switchHomeFragment();
                    list.add(commonEmptyData);
                } else {
                    List<ClientLogBinderManager.LogData> logList = new ArrayList<>(ClientLogBinderManager.getLogData());
                    if (logList.isEmpty()) {
                        CommonEmptyData commonEmptyData = new CommonEmptyData();
                        commonEmptyData.type = CommonEmptyData.TYPE_EMPTY;
                        commonEmptyData.content = getContext().getString(R.string.main_home_empty_content);
                        commonEmptyData.height = recyclerView.getMeasuredHeight();
                        list.add(commonEmptyData);
                    } else {
                        for (ClientLogBinderManager.LogData logData : logList) {
                            list.add(new FreezeHomeLogData(logData.msg));
                        }
                    }
                }
                homeAdapter.updateData(list);
            }
        });
    }

    private void switchHomeFragment() {
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).switchHomeFragment();
        }
    }
}
