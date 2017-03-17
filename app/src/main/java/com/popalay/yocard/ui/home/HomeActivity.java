package com.popalay.yocard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.popalay.yocard.R;
import com.popalay.yocard.databinding.ActivityHomeBinding;
import com.popalay.yocard.ui.base.BaseActivity;
import com.popalay.yocard.ui.cards.CardsFragment;

public class HomeActivity extends BaseActivity {

    private static final int MENU_SETTINGS = Menu.FIRST;

    private ActivityHomeBinding b;
    private HomePagerAdapter pagerAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, "Settings")
                .setIcon(R.drawable.ic_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    private boolean onNavigationClick(MenuItem item) {
        final int nextPosition;
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
        b.host.setCurrentItem(nextPosition, false);
        return true;
    }

    private void initUI() {
        setActionBar(b.toolbar);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.host, CardsFragment.newInstance())
                .commitAllowingStateLoss();

        pagerAdapter = new HomePagerAdapter(getFragmentManager());

        b.host.setAdapter(pagerAdapter);
        b.host.setPagingEnabled(false);
        b.host.setOffscreenPageLimit(pagerAdapter.getCount() - 1);

        b.bottomBar.setOnNavigationItemSelectedListener(this::onNavigationClick);
    }
}
