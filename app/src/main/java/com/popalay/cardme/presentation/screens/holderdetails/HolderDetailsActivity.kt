package com.popalay.cardme.presentation.screens.holderdetails

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.popalay.cardme.App
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityHolderDetailsBinding
import com.popalay.cardme.presentation.base.SlidingActivity
import com.popalay.cardme.presentation.base.navigation.CustomFactory
import com.popalay.cardme.utils.ShareUtils
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class HolderDetailsActivity : SlidingActivity() {

    @Inject lateinit var factory: CustomFactory

    private lateinit var b: ActivityHolderDetailsBinding
    private var isExpanded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        b = DataBindingUtil.setContentView<ActivityHolderDetailsBinding>(this, R.layout.activity_holder_details)

        initUI()
    }

    override fun getRootView(): View = b.root

    override fun canSlideDown() = isExpanded && b.listDebts.scrollY == 0

    private fun initUI() {
        val vm = ViewModelProviders.of(this, factory).get(HolderDetailsViewModel::class.java)
        b.vm = vm

        setSupportActionBar(b.toolbar)
        b.collapsingToolbar.isTitleEnabled = false
        b.toolbar.setNavigationOnClickListener { v -> onBackPressed() }

        vm.doOnShareCard()
                .subscribe { ShareUtils.shareText(this, R.string.share_card, it) }
                .addTo(disposables)

        b.listCards.addItemDecoration(SpacingItemDecoration.Builder(this)
                .onSides(true)
                .betweenItems(true)
                .build())

        b.toolbar.navigationIcon?.alpha = 255
        b.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            isExpanded = verticalOffset == 0
            if (appBarLayout.totalScrollRange == 0) {
                return@addOnOffsetChangedListener
            }
            val alpha = Math.min(-verticalOffset, 255)
            b.toolbar.setTitleTextColor(Color.argb(alpha, 255, 255, 255))
            b.toolbar.navigationIcon?.alpha = alpha
        }

    }

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, HolderDetailsActivity::class.java)
        }
    }
}
