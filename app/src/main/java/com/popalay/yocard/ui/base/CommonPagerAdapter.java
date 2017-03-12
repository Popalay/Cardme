package com.popalay.yocard.ui.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommonPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments;
    private final List<String> titles;
    private final Context context;

    public CommonPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context.getApplicationContext();
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public void add(@NonNull Fragment fragment, @Nullable String title) {
        fragments.add(fragment);
        titles.add(title);
        notifyDataSetChanged();
    }

    public void add(@NonNull Fragment fragment, @StringRes int title) {
        fragments.add(fragment);
        titles.add(context.getString(title));
        notifyDataSetChanged();
    }

    @Override
    @Nullable
    public Fragment getItem(int position) {
        return position < fragments.size() ? fragments.get(position) : null;
    }

    @Override
    @Nullable
    public CharSequence getPageTitle(int position) {
        return position < titles.size() ? titles.get(position) : null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}