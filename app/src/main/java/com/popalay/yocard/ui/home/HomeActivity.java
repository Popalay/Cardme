package com.popalay.yocard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.ActivityHomeBinding;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.base.CommonPagerAdapter;
import com.popalay.yocard.ui.cards.CardsFragment;
import com.popalay.yocard.ui.debts.DebtsFragment;
import com.popalay.yocard.ui.holders.HoldersFragment;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding b;
    private CommonPagerAdapter pagerAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initUI();
    }

    private boolean onNavigationClick(MenuItem item) {
        int nextPosition;
        switch (item.getItemId()) {
            case R.id.cards:
                nextPosition = 0;
                break;
            case R.id.holders:
                nextPosition = 1;
                break;
            case R.id.debts:
                nextPosition = 2;
                break;
            default:
                return false;
        }
        b.host.setCurrentItem(nextPosition);
        return true;
    }

    private void initUI() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.host, CardsFragment.newInstance())
                .commitAllowingStateLoss();

        pagerAdapter = new CommonPagerAdapter(this, getFragmentManager());
        pagerAdapter.add(CardsFragment.newInstance(), null);
        pagerAdapter.add(HoldersFragment.newInstance(), null);
        pagerAdapter.add(DebtsFragment.newInstance(), null);

        b.host.setAdapter(pagerAdapter);
        b.host.setPagingEnabled(false);
        b.bottomBar.setOnNavigationItemSelectedListener(this::onNavigationClick);
    }
}
