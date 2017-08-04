package com.popalay.cardme.business

import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_ADD_DEBT
import com.popalay.cardme.presentation.screens.SCREEN_CARDS
import com.popalay.cardme.presentation.screens.SCREEN_DEBTS
import com.popalay.cardme.presentation.screens.SCREEN_SCAN_CARD
import com.popalay.cardme.presentation.screens.cards.CardsFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShortcutInteractor @Inject constructor(
        private val router: CustomRouter
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
            Shortcut.ADD_DEBT -> {
                router.navigateTo(SCREEN_DEBTS)
                router.navigateTo(SCREEN_ADD_DEBT)
            }
            Shortcut.DEBTS -> router.newRootScreen(SCREEN_DEBTS)
            else -> {
            }
        }
    }

    enum class Shortcut {
        NONE, ADD_CARD, ADD_DEBT, DEBTS
    }
}