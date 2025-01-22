package com.john.freezeapp.freeze;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.john.freezeapp.BaseActivity;
import com.john.freezeapp.FreezeAppManager;
import com.john.freezeapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerActivity extends BaseActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;


    List<View> viewPageViews = new ArrayList<>();

    List<String> tabs = new ArrayList<>();
    List<FreezeAppAdapter> commonAdapters = new ArrayList<>();

    FreezeAppAdapter defrostAppAdapter;
    FreezeAppAdapter freezeAppAdapter;
    FreezeAppAdapter runningAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        if (!isDaemonActive()) {
            finish();
            return;
        }

        tabs.add(getString(R.string.manager_running_app_title));
        tabs.add(getString(R.string.manager_freeze_app_title));
        tabs.add(getString(R.string.manager_defrost_app_title));

        initCommonAdapter();

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpage);

        for (FreezeAppAdapter commonAdapter : commonAdapters) {
            RecyclerView recyclerView = new RecyclerView(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            recyclerView.setAdapter(commonAdapter);
            viewPageViews.add(recyclerView);
        }

        viewPager.setAdapter(new CommonPageAdapter(viewPageViews, tabs));

        requestRunningApp();
        requestEnableApp();
        requestDisableApp();

        tabLayout.setupWithViewPager(viewPager);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
        finish();
    }

    private void initCommonAdapter() {


        defrostAppAdapter = new FreezeAppAdapter(new FreezeAppAdapter.OnItemClick() {
            @Override
            public void onRightClick(FreezeAppManager.AppModel appModel) {
                requestFreezeApp(appModel.packageName);
            }
        });

        freezeAppAdapter = new FreezeAppAdapter(new FreezeAppAdapter.OnItemClick() {
            @Override
            public void onRightClick(FreezeAppManager.AppModel appModel) {
                requestDefrostApp(appModel.packageName);
            }
        });

        runningAdapter = new FreezeAppAdapter(new FreezeAppAdapter.OnItemClick() {
            @Override
            public void onRightClick(FreezeAppManager.AppModel appModel) {
                requestForceStopApp(appModel.packageName);
            }
        });

        commonAdapters.add(runningAdapter);
        commonAdapters.add(defrostAppAdapter);
        commonAdapters.add(freezeAppAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 在这里处理返回按钮的点击事件
                finish(); // 或者其他你想要执行的操作
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void requestEnableApp() {
        showLoading();
        FreezeAppManager.requestEnableApp(this, new FreezeAppManager.Callback() {
            @Override
            public void success(List<FreezeAppManager.AppModel> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        defrostAppAdapter.updateData(getFreezeApps(getResources().getString(R.string.manager_btn_freeze), list));
                    }
                });
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });
    }

    private void requestDisableApp() {
        showLoading();
        FreezeAppManager.requestDisableApp(this, new FreezeAppManager.Callback() {
            @Override
            public void success(List<FreezeAppManager.AppModel> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        freezeAppAdapter.updateData(getFreezeApps(getResources().getString(R.string.manager_btn_defrost), list));
                    }
                });
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });
    }

    private void requestFreezeApp(String packageName) {
        showLoading();
        FreezeAppManager.requestFreezeApp(packageName, new FreezeAppManager.Callback2() {
            @Override
            public void success() {
                hideLoading();
                requestEnableApp();
                requestDisableApp();
                requestRunningApp();
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });
    }

    private void requestDefrostApp(String packageName) {
        showLoading();
        FreezeAppManager.requestDefrostApp(packageName, new FreezeAppManager.Callback2() {
            @Override
            public void success() {
                hideLoading();
                requestEnableApp();
                requestDisableApp();
            }

            @Override
            public void fail() {
                showLoading();
            }
        });
    }

    private void requestRunningApp() {
        showLoading();
        FreezeAppManager.requestRunningApp(this, new FreezeAppManager.Callback3() {
            @Override
            public void success(List<FreezeAppManager.RunningModel> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        List<FreezeAppData> uiList = runningAdapter.getItems();
                        runningAdapter.updateData(getFreezeRunningApp(getResources().getString(R.string.manager_btn_stop),list, uiList));
                    }
                });
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });
    }


    private void requestForceStopApp(String packageName) {
        showLoading();
        FreezeAppManager.requestForceStopApp(packageName, new FreezeAppManager.Callback2() {
            @Override
            public void success() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        requestRunningApp();
                    }
                });
            }

            @Override
            public void fail() {
                hideLoading();
            }
        });
    }

    public static class CommonPageAdapter extends PagerAdapter {

        private final List<View> viewPageViews = new ArrayList<>();
        private final List<String> tabTitles = new ArrayList<>();

        public CommonPageAdapter(List<View> viewPageViews, List<String> tabTitles) {
            this.viewPageViews.addAll(viewPageViews);
            this.tabTitles.addAll(tabTitles);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return this.tabTitles.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = viewPageViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return viewPageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    public List<FreezeAppData> getFreezeApps(String operate, List<FreezeAppManager.AppModel> list) {
        List<FreezeAppData> freezeAppDatas = new ArrayList<>();
        for (FreezeAppManager.AppModel appModel : list) {
            FreezeAppData uiModel = new FreezeAppData();
            uiModel.appModel = appModel;
            uiModel.rightName = operate;
            freezeAppDatas.add(uiModel);
        }
        return freezeAppDatas;
    }


    public List<FreezeAppData> getFreezeRunningApp(String operate,List<FreezeAppManager.RunningModel> list, List<FreezeAppData> uiList) {
        Map<String, FreezeAppData> tempMap = new HashMap<>();

        for (FreezeAppData commonUiModel : uiList) {
            tempMap.put(commonUiModel.appModel.packageName, commonUiModel);
        }
        List<FreezeAppData> newList = new ArrayList<>();
        for (FreezeAppManager.AppModel appModel : list) {
            FreezeAppData uiModel = new FreezeAppData();
            uiModel.appModel = appModel;
            uiModel.rightName = operate;
            if (tempMap.containsKey(appModel.packageName)) {
                uiModel.isProcessExpand = tempMap.get(appModel.packageName).isProcessExpand;
            }
            newList.add(uiModel);
        }
        return newList;
    }
}
