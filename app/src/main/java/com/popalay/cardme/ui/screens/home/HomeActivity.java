package com.popalay.cardme.ui.screens.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.popalay.cardme.Constants;
import com.popalay.cardme.R;
import com.popalay.cardme.data.events.AddCardEvent;
import com.popalay.cardme.data.events.FavoriteHolderEvent;
import com.popalay.cardme.databinding.ActivityHomeBinding;
import com.popalay.cardme.ui.base.BaseActivity;
import com.popalay.cardme.ui.screens.cards.CardsFragment;
import com.popalay.cardme.ui.screens.debts.DebtsFragment;
import com.popalay.cardme.ui.screens.holders.HoldersFragment;
import com.popalay.cardme.ui.screens.settings.SettingsActivity;
import com.popalay.cardme.utils.BrowserUtils;

import org.greenrobot.eventbus.EventBus;

import shortbread.Shortcut;

public class HomeActivity extends BaseActivity implements HomeView {

    private static final int MENU_SETTINGS = Menu.FIRST;

    @InjectPresenter HomePresenter presenter;

    private int startPage = R.id.cards;

    private ActivityHomeBinding b;
    private ActionBarDrawerToggle toggle;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @ProvidePresenter
    HomePresenter providePresenter() {
        return new HomePresenter(startPage);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initUI();
    }

    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, R.string.settings)
                .setIcon(R.drawable.ic_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case MENU_SETTINGS:
                presenter.onSettingsClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void openPage(int pageId) {
        final Fragment fragment;
        switch (pageId) {
            case R.id.cards:
                fragment = CardsFragment.newInstance();
                break;
            case R.id.holders:
                fragment = HoldersFragment.newInstance();
                break;
            case R.id.debts:
                fragment = DebtsFragment.newInstance();
                break;
            default:
                throw new IllegalArgumentException("Illegal position");
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.host, fragment)
                .commit();
    }

    @Override public void openSettings() {
        startActivity(SettingsActivity.getIntent(this));
    }

    @Override public void openPolicy() {
        BrowserUtils.openLink(this, Constants.PRIVACY_POLICY_LINK);
    }

    private boolean onNavigationClick(MenuItem item) {
        presenter.onBottomNavigationItemClick(item.getItemId());
        return true;
    }

    private boolean onDrawerItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_privacy_policy:
                presenter.onPolicyClick();
                break;
        }
        b.drawerLayout.closeDrawers();
        return true;
    }

    private void initUI() {
        setSupportActionBar(b.toolbar);
        toggle = new ActionBarDrawerToggle(this, b.drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        b.drawerLayout.addDrawerListener(toggle);
        b.navigationView.setNavigationItemSelectedListener(this::onDrawerItemClick);
        b.bottomBar.setSelectedItemId(startPage);
        b.bottomBar.setOnNavigationItemSelectedListener(this::onNavigationClick);
    }

    // Shortcuts
    @Shortcut(id = "shortcut_add_card", icon = R.drawable.ic_shortcut_add_card,
            shortLabelRes = R.string.shortcut_add_card)
    public void addCardShortcut() {
        startPage = R.id.cards;
        EventBus.getDefault().postSticky(new AddCardEvent());
    }

    @Shortcut(id = "shortcut_favorite_holder", icon = R.drawable.ic_shortcut_favorite_holder,
            rank = 1, shortLabelRes = R.string.shortcut_favorite_holder)
    public void favoriteHolderShortcut() {
        startPage = R.id.holders;
        EventBus.getDefault().postSticky(new FavoriteHolderEvent());
    }

    @Shortcut(id = "shortcut_debts", icon = R.drawable.ic_shortcut_debts,
            rank = 2, shortLabelRes = R.string.shortcut_debts)
    public void debtsShortcut() {
        startPage = R.id.debts;
    }
}
