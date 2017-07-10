package com.popalay.cardme.presentation.screens.home

import com.arellomobile.mvp.InjectViewState
import com.popalay.cardme.App
import com.popalay.cardme.R
import com.popalay.cardme.presentation.SCREEN_CARDS
import com.popalay.cardme.presentation.SCREEN_DEBTS
import com.popalay.cardme.presentation.SCREEN_HOLDERS
import com.popalay.cardme.presentation.SCREEN_SETTINGS
import com.popalay.cardme.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class HomePresenter(startPageId: Int) : BasePresenter<HomeView>() {

    @Inject lateinit var router: Router

    init {
        App.appComponent.inject(this)
        openByPage(startPageId)
    }

    fun onBottomNavigationItemClick(pageId: Int) {
        openByPage(pageId)
    }

    fun onSettingsClick() {
        router.navigateTo(SCREEN_SETTINGS)
    }

    fun onPolicyClick() {
        viewState.openPolicy()
    }

    private fun openByPage(pageId: Int) {
        when (pageId) {
            R.id.cards -> router.replaceScreen(SCREEN_CARDS)
            R.id.holders -> router.replaceScreen(SCREEN_HOLDERS)
            R.id.debts -> router.replaceScreen(SCREEN_DEBTS)
        }
    }

}
