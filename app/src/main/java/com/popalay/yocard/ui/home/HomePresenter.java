package com.popalay.yocard.ui.home;

import com.arellomobile.mvp.InjectViewState;
import com.popalay.yocard.ui.base.BasePresenter;

@InjectViewState
public class HomePresenter extends BasePresenter<HomeView> {

    private final int startPageId;

    public HomePresenter(int startPageId) {
        this.startPageId = startPageId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().openPage(startPageId);
        getViewState().setBottomNavigationItemSelected(startPageId);
    }

    public void onBottomNavigationItemClick(int itemId) {
        getViewState().openPage(itemId);
    }

    public void onDrawerItemClick(int itemId) {

    }
}
