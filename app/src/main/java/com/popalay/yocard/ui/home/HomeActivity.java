package com.popalay.yocard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.ActivityHomeBinding;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.cards.CardsFragment;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding b;
    private TabsPagerAdapter pagerAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initUI();
    }

    private void initUI() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.host, CardsFragment.newInstance())
                .commitAllowingStateLoss();

        pagerAdapter = new TabsPagerAdapter(getFragmentManager());
        b.host.setAdapter(pagerAdapter);
        b.host.setPagingEnabled(false);
        b.bottomBar.setOnNavigationItemSelectedListener(item -> {
            int nextPosition = 0;
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
            }
            b.host.setCurrentItem(nextPosition);
            return true;
        });
    }
}
