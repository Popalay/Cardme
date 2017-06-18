package com.popalay.cardme.presentation.screens.home;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.cardme.presentation.base.BasePresenter;

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
