package com.john.freezeapp.freeze;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FreezeAppPageAdapter extends PagerAdapter {

    private final List<View> viewPageViews = new ArrayList<>();
    private final List<String> tabTitles = new ArrayList<>();

    public FreezeAppPageAdapter(List<View> viewPageViews, List<String> tabTitles) {
        this.viewPageViews.addAll(viewPageViews);
        this.tabTitles.addAll(tabTitles);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

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