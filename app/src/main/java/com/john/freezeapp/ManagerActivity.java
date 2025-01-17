package com.john.freezeapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ManagerActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    RelativeLayout loadingView;
    Toolbar toolbar;

    AtomicInteger loadingInteger = new AtomicInteger(0);

    List<View> viewPageViews = new ArrayList<>();

    List<String> tabs = new ArrayList<>();
    List<CommonAdapter> commonAdapters = new ArrayList<>();

    CommonAdapter defrostAppAdapter;


    CommonAdapter freezeAppAdapter ;

    CommonAdapter runningAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        tabs.add(getString(R.string.manager_running_app_title));
        tabs.add(getString(R.string.manager_freeze_app_title));
        tabs.add(getString(R.string.manager_defrost_app_title));

        initCommonAdapter();

        tabLayout = findViewById(R.id.tablayout);
        loadingView = findViewById(R.id.loading);
        viewPager = findViewById(R.id.viewpage);

        for (CommonAdapter commonAdapter : commonAdapters) {
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

    private void initCommonAdapter() {
        defrostAppAdapter = new CommonAdapter(getString(R.string.manager_btn_freeze), new CommonAdapter.OnItemClick() {
            @Override
            public void onRightClick(FreezeAppManager.AppModel appModel) {
                requestFreezeApp(appModel.packageName);
            }
        });

        freezeAppAdapter = new CommonAdapter(getString(R.string.manager_btn_defrost), new CommonAdapter.OnItemClick() {
            @Override
            public void onRightClick(FreezeAppManager.AppModel appModel) {
                requestDefrostApp(appModel.packageName);
            }
        });

        runningAdapter = new CommonAdapter(getString(R.string.manager_btn_stop), new CommonAdapter.OnItemClick() {
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
                        defrostAppAdapter.update(list);
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
                        freezeAppAdapter.update(list);
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
                        runningAdapter.update2(list);
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

    public static class CommonUIModel {
        public FreezeAppManager.AppModel appModel;
        public boolean isProcessExpand = false;
        public LinearLayout cacheView;
    }

    public static class CommonAdapter extends RecyclerView.Adapter<CommonViewHolder> {
        public String mRightBtnName = "";
        public OnItemClick mRightBtnClickListener;


        public void update(List<FreezeAppManager.AppModel> list) {

            comonUiModels.clear();
            for (FreezeAppManager.AppModel appModel : list) {
                CommonUIModel uiModel = new CommonUIModel();
                uiModel.appModel = appModel;
                comonUiModels.add(uiModel);
            }
            notifyDataSetChanged();
        }


        public void update2(List<FreezeAppManager.RunningModel> list) {
            Map<String, CommonUIModel> tempMap = new HashMap<>();
            for (CommonUIModel commonUiModel : comonUiModels) {
                tempMap.put(commonUiModel.appModel.packageName, commonUiModel);
            }
            comonUiModels.clear();
            for (FreezeAppManager.AppModel appModel : list) {
                CommonUIModel uiModel = new CommonUIModel();
                uiModel.appModel = appModel;
                if (tempMap.containsKey(appModel.packageName)) {
                    uiModel.isProcessExpand = tempMap.get(appModel.packageName).isProcessExpand;
                }
                comonUiModels.add(uiModel);
            }
            notifyDataSetChanged();
        }

        public interface OnItemClick {
            void onRightClick(FreezeAppManager.AppModel appModel);
        }

        CommonAdapter(String rightBtnName, OnItemClick onClickListener) {
            mRightBtnName = rightBtnName;
            mRightBtnClickListener = onClickListener;
        }

        List<CommonUIModel> comonUiModels = new ArrayList<>();

        @NonNull
        @Override
        public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
            return new CommonViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
            CommonUIModel commonUIModel = comonUiModels.get(position);
            if (commonUIModel.appModel.icon != null) {
                holder.ivIcon.setImageDrawable(commonUIModel.appModel.icon);
            }
            if (commonUIModel.appModel.name != null) {
                holder.tvName.setText(commonUIModel.appModel.name);
            }
            holder.tvOperate.setText(mRightBtnName);
            holder.tvOperate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRightBtnClickListener.onRightClick(commonUIModel.appModel);
                }
            });

            if (commonUIModel.appModel instanceof FreezeAppManager.RunningModel) {
                holder.appContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonUIModel.isProcessExpand = !commonUIModel.isProcessExpand;
                        notifyDataSetChanged();
                    }
                });
            }

            if (commonUIModel.appModel instanceof FreezeAppManager.RunningModel) {
                if (commonUIModel.cacheView == null) {
                    Context context = holder.llProcess.getContext();
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    for (FreezeAppManager.ProcessModel processModel : ((FreezeAppManager.RunningModel) commonUIModel.appModel).processModels) {
                        View processView = LayoutInflater.from(context).inflate(R.layout.process_info, null);
                        TextView processName = processView.findViewById(R.id.tv_process_name);
                        TextView processTime = processView.findViewById(R.id.tv_process_time);
                        processName.setText(processModel.processName);
                        processTime.setText("CPU - " + processModel.time);
                        linearLayout.addView(processView);
                    }
                    commonUIModel.cacheView = linearLayout;
                }

                holder.llProcess.removeAllViews();
                ViewParent parent = commonUIModel.cacheView.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeAllViews();
                }
                holder.llProcess.removeView(commonUIModel.cacheView);
                holder.llProcess.setVisibility(View.GONE);

                if (commonUIModel.isProcessExpand) {
                    holder.llProcess.setVisibility(View.VISIBLE);
                    holder.llProcess.addView(commonUIModel.cacheView);
                }
            }

        }

        @Override
        public int getItemCount() {
            return comonUiModels.size();
        }
    }

    public static class CommonViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ImageView ivIcon;
        public TextView tvOperate;
        public LinearLayout llProcess;
        public ViewGroup appContainer;


        public CommonViewHolder(@NonNull View itemView) {
            super(itemView);
            appContainer = itemView.findViewById(R.id.app_container);
            ivIcon = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvOperate = itemView.findViewById(R.id.tv_operate);
            llProcess = itemView.findViewById(R.id.process_info);
        }
    }

    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingInteger.getAndIncrement();
                loadingView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingInteger.getAndDecrement();
                if (loadingInteger.get() == 0) {
                    loadingView.setVisibility(View.GONE);
                }
            }
        });
    }
}
