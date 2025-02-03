package com.john.freezeapp;

import android.app.AppOpsManagerHidden;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.SharedPrefUtil;
import com.john.hidden.api.Replace;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FreezeUtil.generateShell(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (R.id.menu_stop_server == itemId) {
            showStopDaemonDialog();
            return true;
        } else if(R.id.menu_developer == itemId) {
            FreezeUtil.toDevelopPage(getContext());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindDaemon(IDaemonBinder daemonBinder) {
        super.bindDaemon(daemonBinder);
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

    private void showStopDaemonDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.freeze_stop_daemon_title)
                .setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FreezeUtil.stopDaemon();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
