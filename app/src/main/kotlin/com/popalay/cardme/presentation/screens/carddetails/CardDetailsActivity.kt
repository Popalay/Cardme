package com.popalay.cardme.presentation.screens.carddetails

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.DURATION_SHORT
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityCardDetailsBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.onEnd
import com.popalay.cardme.utils.extensions.showAnimated
import javax.inject.Inject

class CardDetailsActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityCardDetailsBinding

    override var navigator = object : CustomNavigator(this) {
        override fun exit() = exitWithAnimation()
    }

    companion object {

        const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"

        fun getIntent(context: Context, number: String) = Intent(context, CardDetailsActivity::class.java).apply {
            putExtra(KEY_CARD_NUMBER, number)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityCardDetailsBinding>(this, R.layout.activity_card_details)
        ViewModelProviders.of(this, factory).get(CardDetailsViewModel::class.java).let {
            b.vm = it
        }
        initUi()
    }

    override fun onBackPressed() = exitWithAnimation()

    private fun exitWithAnimation() {
        b.buttonEdit.hideAnimated()
        b.buttonNfc.hideAnimated(DURATION_SHORT / 2)
        b.buttonShare.hideAnimated(DURATION_SHORT) { supportFinishAfterTransition() }
    }

    private fun initUi() {
        window.sharedElementEnterTransition.onEnd {
            b.buttonShare.showAnimated()
            b.buttonNfc.showAnimated(DURATION_SHORT / 2)
            b.buttonEdit.showAnimated(DURATION_SHORT)
        }
    }

}
