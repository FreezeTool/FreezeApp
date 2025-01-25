package com.john.freezeapp;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;
import com.john.freezeapp.freeze.ManagerActivity;
import com.john.freezeapp.home.FreezeHomeBillboardData;
import com.john.freezeapp.home.FreezeHomeDaemonData;
import com.john.freezeapp.home.FreezeHomeData;
import com.john.freezeapp.home.FreezeHomeDeviceData;
import com.john.freezeapp.home.FreezeHomeAdapter;
import com.john.freezeapp.home.FreezeHomeFuncData;
import com.john.freezeapp.home.FreezeHomeFuncHelper;
import com.john.freezeapp.home.FuncFragment;
import com.john.freezeapp.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import rikka.shizuku.Shizuku;


public class MainActivity extends BaseActivity {

    Toolbar toolbar;

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
        toolbar = findViewById(R.id.toolbar);
        FreezeUtil.generateShell(this);
        setSupportActionBar(toolbar);
        tabs.add(new Tab(R.id.navigation_home, new HomeFragment()));
        tabs.add(new Tab(R.id.navigation_func, new FuncFragment()));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.viewPager);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindDaemon(IDaemonBinderContainer daemonBinderContainer) {
        super.bindDaemon(daemonBinderContainer);
        postUI(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(1);
            }
        });
    }

    @Override
    protected void unbindDaemon() {
        super.unbindDaemon();
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
