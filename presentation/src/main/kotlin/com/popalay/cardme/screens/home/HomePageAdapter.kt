/*
 * Created by popalay on 04.01.18 21:16
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 04.01.18 21:16
 */

package com.popalay.cardme.screens.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.popalay.cardme.screens.cards.CardsFragment
import com.popalay.cardme.screens.debts.DebtsFragment

/**
 * Created by Denys Nykyforov on 04.01.2018
 * Copyright (c) 2018. All right reserved
 */
class HomePageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    companion object {

        private const val PAGE_COUNT = 2
    }

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> CardsFragment.newInstance()
        1 -> DebtsFragment.newInstance()
        else -> throw IllegalArgumentException("Can not create fragment fro position $position")
    }

    override fun getCount() = PAGE_COUNT
}