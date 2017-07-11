package com.popalay.cardme.presentation.screens.home

import com.arellomobile.mvp.InjectViewState
import com.popalay.cardme.App
import com.popalay.cardme.R
import com.popalay.cardme.business.ShortcutInteractor
import com.popalay.cardme.presentation.base.BasePresenter
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_CARDS
import com.popalay.cardme.presentation.screens.SCREEN_DEBTS
import com.popalay.cardme.presentation.screens.SCREEN_HOLDERS
import com.popalay.cardme.presentation.screens.SCREEN_SETTINGS
import javax.inject.Inject

@InjectViewState
class HomePresenter(startPageId: Int) : BasePresenter<HomeView>() {

    @Inject lateinit var router: CustomRouter
    @Inject lateinit var shortcutInteractor: ShortcutInteractor

    init {
        App.appComponent.inject(this)
        if (!shortcutInteractor.startedWithShortcut()) openByPage(startPageId)
    }

    fun onBottomNavigationItemClick(pageId: Int) = openByPage(pageId)

    fun onSettingsClick() = router.navigateTo(SCREEN_SETTINGS)

    fun onPolicyClick() = viewState.openPolicy()

    private fun openByPage(pageId: Int) {
        when (pageId) {
            R.id.cards -> router.replaceScreen(SCREEN_CARDS)
            R.id.holders -> router.replaceScreen(SCREEN_HOLDERS)
            R.id.debts -> router.replaceScreen(SCREEN_DEBTS)
        }
    }

}
