package com.popalay.yocard.ui.home;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.ui.base.BasePresenter;

@InjectViewState
public class HomePresenter extends BasePresenter<HomeView> {

    public HomePresenter(int startPageId) {
        getViewState().openPage(startPageId);
    }

    public void onBottomNavigationItemClick(int itemId) {
        getViewState().openPage(itemId);
    }

    public void onDrawerItemClick(int itemId) {

    }
}
