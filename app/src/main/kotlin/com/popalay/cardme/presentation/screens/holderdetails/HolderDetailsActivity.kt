package com.popalay.cardme.presentation.screens.holderdetails

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityHolderDetailsBinding
import com.popalay.cardme.presentation.base.RightSlidingActivity
import com.popalay.cardme.utils.ShareUtils
import com.popalay.cardme.utils.extensions.onItemTouch
import com.popalay.cardme.utils.recycler.SpacingItemDecoration
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class HolderDetailsActivity : RightSlidingActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityHolderDetailsBinding
    private lateinit var viewModelFacade: HolderDetailsViewModelFacade

    private var isCardTouched: Boolean = false

    companion object {

        const val KEY_HOLDER_DETAILS = "KEY_HOLDER_DETAILS"

        fun getIntent(context: Context, name: String) = Intent(context, HolderDetailsActivity::class.java).apply {
            putExtra(KEY_HOLDER_DETAILS, name)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityHolderDetailsBinding>(this, R.layout.activity_holder_details)
        ViewModelProviders.of(this, factory).get(HolderDetailsViewModel::class.java).let {
            b.vm = it
            viewModelFacade = it
        }
        initUI()
    }

    override fun getRootView(): View = b.root

    override fun canSlideRight() = !b.listCards.isAnimating
            && (b.listCards.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
            || !isCardTouched
            || b.listCards.childCount == 0

    private fun initUI() {
        setSupportActionBar(b.toolbar)
        b.collapsingToolbar.isTitleEnabled = false

        viewModelFacade.doOnShareCard()
                .subscribe {
                    ShareUtils.shareText(this, R.string.share_card, it)
                }.addTo(disposables)

        b.listCards.addItemDecoration(SpacingItemDecoration.create {
            dividerSize = resources.getDimension(R.dimen.normal).toInt()
            showBetween = true
            showOnSides = true
        })

        b.listCards.onItemTouch {
            when (it.action) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> isCardTouched = true
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> isCardTouched = false
            }
        }

        b.appBar.addOnOffsetChangedListener {
            appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange == 0) {
                return@addOnOffsetChangedListener
            }
            val alpha = Math.min(-verticalOffset, 255)
            b.toolbar.setTitleTextColor(Color.argb(alpha, 255, 255, 255))
        }

    }

}
