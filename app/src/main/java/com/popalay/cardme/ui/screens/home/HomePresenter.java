package com.popalay.cardme.ui.screens.home;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.ui.base.BasePresenter;

@InjectViewState
public class HomePresenter extends BasePresenter<HomeView> {

    public HomePresenter(int startPageId) {
        getViewState().openPage(startPageId);
    }

    public void onBottomNavigationItemClick(int itemId) {
        getViewState().openPage(itemId);
    }

    public void onSettingsClick() {
        getViewState().openSettings();
    }

    public void onPolicyClick() {
        getViewState().openPolicy();
    }
}
