package com.popalay.yocard.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.popalay.yocard.ui.cards.CardsFragment;
import com.popalay.yocard.ui.debts.DebtsFragment;
import com.popalay.yocard.ui.holders.HoldersFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 3;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CardsFragment.newInstance();
            case 1:
                return HoldersFragment.newInstance();
            case 2:
                return DebtsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
