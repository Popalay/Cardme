package com.popalay.cardme.presentation.screens.cards

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.databinding.FragmentCardsBinding
import com.popalay.cardme.presentation.base.BaseFragment
import com.popalay.cardme.utils.ShareUtils
import com.popalay.cardme.utils.recycler.SimpleItemTouchHelperCallback
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration
import dagger.android.support.AndroidSupportInjection
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class CardsFragment : BaseFragment(), SimpleItemTouchHelperCallback.SwipeCallback, SimpleItemTouchHelperCallback.DragCallback {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: FragmentCardsBinding
    private lateinit var viewModelFacade: CardsViewModelFacade

    companion object {

        const val SCAN_REQUEST_CODE = 121

        fun newInstance() = CardsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
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
                //presenter.onCardScanned(scanResult)
            }
        }
    }

    fun showRemoveUndoAction(card: Card) {
/*        Snackbar.make(b.listCards, R.string.card_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo) { presenter.onRemoveUndo(card) }
                .show()*/
    }

    override fun onSwiped(position: Int) {
/*        val card = b.vm.get(position)
        presenter.onItemSwiped(card)*/
    }

    override fun onDragged(from: Int, to: Int) {
        //presenter.onItemDragged(b.vm.cards, from, to)
    }

    override fun onDropped() {
        //presenter.onItemDropped(b.vm.cards)
    }

    private fun initUI() {
        b.listCards.addItemDecoration(SpacingItemDecoration.Builder(context)
                .firstDivider(true)
                .lastDivider(true)
                .betweenItems(true)
                .onSides(true)
                .build())

        ItemTouchHelper(SimpleItemTouchHelperCallback(this, this))
                .attachToRecyclerView(b.listCards)

        viewModelFacade.doOnShareCard()
                .subscribe {
                    ShareUtils.shareText(activity, R.string.share_card, it)
                }.addTo(disposables)
    }
}
