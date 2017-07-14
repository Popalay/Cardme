package com.popalay.cardme.presentation.screens.home

import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.PRIVACY_POLICY_LINK
import com.popalay.cardme.R
import com.popalay.cardme.business.ShortcutInteractor
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_CARDS
import com.popalay.cardme.presentation.screens.SCREEN_DEBTS
import com.popalay.cardme.presentation.screens.SCREEN_HOLDERS
import com.popalay.cardme.presentation.screens.SCREEN_SETTINGS
import com.popalay.cardme.utils.extensions.applyThrottling
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        shortcutInteractor: ShortcutInteractor,
        private val router: CustomRouter
) : BaseViewModel() {

    val settingClick: PublishRelay<Boolean> = PublishRelay.create<Boolean>()
    val bottomNavigationClick: PublishRelay<Int> = PublishRelay.create<Int>()
    val drawerNavigationClick: PublishRelay<Int> = PublishRelay.create<Int>()

    init {
        if (!shortcutInteractor.startedWithShortcut())
            router.newRootScreen(SCREEN_CARDS)

        settingClick.applyThrottling()
                .doOnNext { router.navigateTo(SCREEN_SETTINGS) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        bottomNavigationClick.applyThrottling()
                .doOnNext(this::openByPage)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        drawerNavigationClick.applyThrottling()
                .doOnNext(this::openByPage)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

    private fun openByPage(pageId: Int) {
        when (pageId) {
            R.id.cards -> router.newRootScreen(SCREEN_CARDS)
            R.id.holders -> router.newRootScreen(SCREEN_HOLDERS)
            R.id.debts -> router.newRootScreen(SCREEN_DEBTS)
            R.id.navigation_privacy_policy -> router.navigateToUrl(PRIVACY_POLICY_LINK)
        }
    }
}