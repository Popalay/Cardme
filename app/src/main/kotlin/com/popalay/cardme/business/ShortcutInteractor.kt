package com.popalay.cardme.business

import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.*
import com.popalay.cardme.presentation.screens.cards.CardsFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortcutInteractor @Inject constructor(
        private val router: CustomRouter,
        private val holderInteractor: HolderInteractor
) {

    private var appliedShortcut = Shortcut.NONE
        get() {
            val currentValue = field
            appliedShortcut = Shortcut.NONE
            return currentValue
        }

    fun startedWithShortcut() = appliedShortcut != Shortcut.NONE

    fun applyShortcut(shortcut: Shortcut) {
        appliedShortcut = shortcut
        when (shortcut) {
            Shortcut.ADD_CARD -> {
                router.newRootScreen(SCREEN_CARDS)
                router.navigateToForResult(SCREEN_SCAN_CARD, requestCode = CardsFragment.SCAN_REQUEST_CODE)
            }
            Shortcut.FAVORITE_HOLDER -> {
                router.newRootScreen(SCREEN_HOLDERS)
                holderInteractor.getFavoriteHolder()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess { router.navigateTo(SCREEN_HOLDER_DETAILS, it.id) }
                        .subscribeBy(Throwable::printStackTrace)
            }
            Shortcut.DEBTS -> router.newRootScreen(SCREEN_DEBTS)
            else -> {
            }
        }
    }

    enum class Shortcut {
        NONE, ADD_CARD, FAVORITE_HOLDER, DEBTS
    }
}