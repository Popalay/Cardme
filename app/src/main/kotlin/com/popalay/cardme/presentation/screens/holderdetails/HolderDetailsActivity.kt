package com.popalay.cardme.presentation.screens.holderdetails

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityHolderDetailsBinding
import com.popalay.cardme.presentation.base.RightSlidingActivity
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.onItemTouch
import com.popalay.cardme.utils.extensions.openShareChooser
import com.popalay.cardme.utils.recycler.SpacingItemDecoration
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class HolderDetailsActivity : RightSlidingActivity() {

    companion object {
        const val KEY_HOLDER_DETAILS = "KEY_HOLDER_DETAILS"
        fun getIntent(context: Context, name: String) = Intent(context, HolderDetailsActivity::class.java).apply {
            putExtra(KEY_HOLDER_DETAILS, name)
        }
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    @Inject lateinit var viewModelFacade: HolderDetailsViewModelFacade

    private lateinit var b: ActivityHolderDetailsBinding

    private var isCardTouched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding<ActivityHolderDetailsBinding>(R.layout.activity_holder_details)
        b.vm = getViewModel<HolderDetailsViewModel>(factory)
        initUI()
    }

    override fun getRootView(): View = b.root

    override fun canSlideRight() = !b.listCards.isAnimating
            && (b.listCards.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
            || !isCardTouched
            || b.listCards.childCount == 0

    private fun initUI() {
        setSupportActionBar(b.toolbar)

        viewModelFacade.doOnShareCard()
                .subscribe { openShareChooser(R.string.share_card, it) }
                .addTo(disposables)

        b.listCards.addItemDecoration(SpacingItemDecoration.create {
            dividerSize = resources.getDimension(R.dimen.normal).toInt()
            showBetween = true
            showOnSides = true
            onTop = false
        })

        b.listCards.onItemTouch {
            when (it.action) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> isCardTouched = true
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> isCardTouched = false
            }
        }

    }

}
