package com.popalay.cardme.screens.home

import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.PRIVACY_POLICY_LINK
import com.popalay.cardme.R
import com.popalay.cardme.domain.interactor.ShortcutInteractor
import com.popalay.cardme.domain.interactor.CardInteractor
import com.popalay.cardme.domain.interactor.HolderInteractor
import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.screens.*
import com.popalay.cardme.utils.extensions.applyThrottling
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.nio.charset.Charset
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        shortcutInteractor: ShortcutInteractor,
        private val holderInteractor: HolderInteractor,
        private val cardInteractor: CardInteractor,
        private val router: CustomRouter
) : BaseViewModel(), HomeViewModelFacade {

    val settingClick: PublishRelay<Boolean> = PublishRelay.create<Boolean>()
    val bottomNavigationClick: PublishRelay<Int> = PublishRelay.create<Int>()
    val drawerNavigationClick: PublishRelay<Int> = PublishRelay.create<Int>()

    init {
        if (!shortcutInteractor.startedWithShortcut()) router.newRootScreen(SCREEN_CARDS)

        settingClick
                .applyThrottling()
                .doOnNext { router.navigateTo(SCREEN_SETTINGS) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        bottomNavigationClick
                .distinctUntilChanged()
                .doOnNext(this::openByPage)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        drawerNavigationClick
                .applyThrottling()
                .doOnNext(this::openByPage)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

    private fun openByPage(pageId: Int) {
        when (pageId) {
            R.id.cards -> router.replaceScreen(SCREEN_CARDS)
            R.id.holders -> router.replaceScreen(SCREEN_HOLDERS)
            R.id.debts -> router.replaceScreen(SCREEN_DEBTS)
            R.id.navigation_trash -> router.navigateTo(SCREEN_TRASH)
            R.id.navigation_privacy_policy -> router.navigateToUrl(PRIVACY_POLICY_LINK)
        }
    }

    override fun onNfcMessageRead(message: ByteArray) {
        cardInteractor.getFromJson(message.toString(Charset.defaultCharset()))
                .flatMapCompletable(holderInteractor::addCard)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

    override fun onSettingClick() = settingClick.accept(true)
}

interface HomeViewModelFacade {

    fun onNfcMessageRead(message: ByteArray)
    fun onSettingClick()

}