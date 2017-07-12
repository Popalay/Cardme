package com.popalay.cardme.presentation.screens.holderdetails

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityHolderDetailsBinding
import com.popalay.cardme.presentation.base.SlidingActivity
import com.popalay.cardme.utils.ShareUtils
import com.popalay.cardme.utils.recycler.decoration.SpacingItemDecoration
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class HolderDetailsActivity : SlidingActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityHolderDetailsBinding
    private lateinit var viewModelFacade: HolderDetailsViewModelFacade

    private var isExpanded: Boolean = false

    companion object {

        const val KEY_HOLDER_DETAILS = "KEY_HOLDER_DETAILS"

        fun getIntent(context: Context, id: String): Intent {
            val intent = Intent(context, HolderDetailsActivity::class.java)
            intent.putExtra(KEY_HOLDER_DETAILS, id)
            return intent
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

    override fun canSlideDown() = isExpanded && b.listDebts.scrollY == 0

    private fun initUI() {
        setSupportActionBar(b.toolbar)
        b.collapsingToolbar.isTitleEnabled = false
        b.toolbar.setNavigationOnClickListener { v -> onBackPressed() }

        viewModelFacade.doOnShareCard()
                .subscribe {
                    ShareUtils.shareText(this, R.string.share_card, it)
                }.addTo(disposables)

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

}
