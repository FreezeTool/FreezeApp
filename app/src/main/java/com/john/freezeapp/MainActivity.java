package com.john.freezeapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.home.FuncFragment;
import com.john.freezeapp.home.HomeFragment;
import com.john.freezeapp.home.LogFragment;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.SharedPrefUtil;
import com.john.freezeapp.util.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class MainActivity extends ToolbarActivity {

    ViewPager viewPager;

    private final List<Tab> tabs = new ArrayList<>();

    public static class Tab {
        public int menuId;
        public Fragment fragment;

        public Tab(int menuId, Fragment fragment) {
            this.fragment = fragment;
            this.menuId = menuId;
        }
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FreezeUtil.generateShell(this);
        tabs.add(new Tab(R.id.navigation_home, new HomeFragment()));
        tabs.add(new Tab(R.id.navigation_func, new FuncFragment()));
        tabs.add(new Tab(R.id.navigation_log, new LogFragment()));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(tabs.size());
        viewPager.setAdapter(new MainAdapter(tabs, getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (int i = 0; i < tabs.size(); i++) {
                    if (tabs.get(i).menuId == item.getItemId()) {
                        viewPager.setCurrentItem(i);
                        return true;
                    }
                }
                return false;
            }
        });
        initPackage();

    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    public static class MainAdapter extends FragmentPagerAdapter {
        List<Tab> fragments;

        public MainAdapter(List<Tab> tabs, @NonNull FragmentManager fm) {
            super(fm);
            this.fragments = tabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position).fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void bindDaemon(IDaemonBinder daemonBinder) {
        super.bindDaemon(daemonBinder);
        initPackage();
        if (SharedPrefUtil.isFirstBindDaemon()) {
            postUI(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(1);
                }
            });
            SharedPrefUtil.setFirstBindDaemon();
        }

    }

    private void initPackage() {
        if (!isDaemonActive()) {
            return;
        }
        FreezeAppManager.requestAppList(getContext(), FreezeAppManager.TYPE_ALL, FreezeAppManager.STATUS_ALL, true, new FreezeAppManager.Callback() {
            @Override
            public void success(List<FreezeAppManager.AppModel> list) {
                ExecutorService executorService = ThreadPool.createExecutorService(4);
                ClientLog.log("initPackage - start");
                List<List<FreezeAppManager.AppModel>> lists = FreezeUtil.averageAssign(list, 4);
                for (List<FreezeAppManager.AppModel> appModels : lists) {
                    executorService.execute(() -> {
                        for (FreezeAppManager.AppModel appModel : appModels) {
                            if (!TextUtils.isEmpty(appModel.packageName)) {
                                FreezeAppManager.getAppModel(getContext(), appModel.packageName);
                            }
                        }
                        ClientLog.log("initPackage - done");
                    });
                }


                executorService.shutdown();

            }

            @Override
            public void fail() {

            }
        });
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
//        if (SharedPrefUtil.isFirstUnbindDaemon()) {
//            SharedPrefUtil.setFirstUnbindDaemon();
//        }
        postUI(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(0);
            }
        });
    }
}
