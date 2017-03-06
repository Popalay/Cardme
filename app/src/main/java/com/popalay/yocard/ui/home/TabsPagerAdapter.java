package com.popalay.yocard.ui.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.popalay.yocard.ui.cards.CardsFragment;
import com.popalay.yocard.ui.holders.HoldersFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> pages;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        pages = new ArrayList<>(3);
        pages.add(CardsFragment.newInstance());
        pages.add(HoldersFragment.newInstance());
        pages.add(CardsFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
