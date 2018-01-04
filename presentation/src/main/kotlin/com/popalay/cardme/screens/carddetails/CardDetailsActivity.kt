package com.popalay.cardme.screens.carddetails

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import com.popalay.cardme.DURATION_SHORT
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseActivity
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.databinding.ActivityCardDetailsBinding
import com.popalay.cardme.utils.extensions.createNdefMessage
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.onEnd
import com.popalay.cardme.utils.extensions.openShareChooser
import com.popalay.cardme.utils.extensions.shareUsingNfc
import com.popalay.cardme.utils.extensions.showAnimated
import javax.inject.Inject


class CardDetailsActivity : BaseActivity(), NfcAdapter.CreateNdefMessageCallback {

    companion object {
        const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"
        fun getIntent(context: Context, number: String) = Intent(context, CardDetailsActivity::class.java).apply {
            putExtra(KEY_CARD_NUMBER, number)
        }
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var viewModelFacade: CardDetailsViewModelFacade
    @Inject override lateinit var navigator: CustomNavigator

    private lateinit var b: ActivityCardDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding(R.layout.activity_card_details)
        b.vm = getViewModel(factory)
        NfcAdapter.getDefaultAdapter(this)?.setNdefPushMessageCallback(this, this)
        initUi()
    }

    override fun createNdefMessage(event: NfcEvent?) =
            createNdefMessage(viewModelFacade.getCardShareNfcObject().blockingGet().toByteArray())

    override fun onBackPressed() {
        exitWithAnimation()
    }

    fun exitWithAnimation() {
        b.buttonRemove.hideAnimated()
        b.buttonEdit.hideAnimated(DURATION_SHORT / 3)
        b.buttonNfc.hideAnimated(2 * DURATION_SHORT / 3)
        b.buttonShare.hideAnimated(DURATION_SHORT) { supportFinishAfterTransition() }
    }

    private fun initUi() {
        window.sharedElementEnterTransition.onEnd {
            listOf(b.buttonRemove, b.buttonEdit, b.buttonNfc, b.buttonShare)
                    .reversed()
                    .forEachIndexed { index, view -> view.showAnimated(index * DURATION_SHORT / 3) }
        }

        viewModelFacade.onShareCard()
                .bindToLifecycle()
                .subscribe { openShareChooser(R.string.share_card, it) }

        viewModelFacade.onShareCardUsingNfc()
                .bindToLifecycle()
                .subscribe { shareUsingNfc(R.string.share_card, it) }
    }

}
