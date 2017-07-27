package com.popalay.cardme.presentation.screens.cards

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.databinding.FragmentCardsBinding
import com.popalay.cardme.presentation.base.BaseFragment
import com.popalay.cardme.utils.ShareUtils
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class CardsFragment : BaseFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: FragmentCardsBinding
    private lateinit var viewModelFacade: CardsViewModelFacade

    companion object {

        const val SCAN_REQUEST_CODE = 121

        fun newInstance() = CardsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_cards, container, false)
        ViewModelProviders.of(this, factory).get(CardsViewModel::class.java).let {
            b.vm = it
            viewModelFacade = it
        }
        initUI()
        return b.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
                viewModelFacade.onCardScanned(scanResult)
            }
        }
    }

    private fun initUI() {
        b.listCards.addItemDecoration(SpacingItemDecoration.create {
            dividerSize = resources.getDimension(R.dimen.normal).toInt()
            showBetween = true
            showOnSides = true
        })

        viewModelFacade.doOnShareCard()
                .subscribe { ShareUtils.shareText(activity, R.string.share_card, it) }
                .addTo(disposables)
    }
}
