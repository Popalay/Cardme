package com.popalay.cardme.presentation.screens.carddetails

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import com.popalay.cardme.DURATION_SHORT
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityCardDetailsBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.extensions.*
import io.reactivex.rxkotlin.addTo
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

    override fun createNdefMessage(event: NfcEvent?) = createNdefMessage(
            viewModelFacade.getCardShareNfcObject().blockingGet().toByteArray())

    override fun onBackPressed() = exitWithAnimation()

    fun exitWithAnimation() {
        b.buttonRemove.hideAnimated()
        b.buttonEdit.hideAnimated(DURATION_SHORT / 3)
        b.buttonNfc.hideAnimated(2 * DURATION_SHORT / 3)
        b.buttonShare.hideAnimated(DURATION_SHORT) { supportFinishAfterTransition() }
    }

    private fun initUi() {
        window.sharedElementEnterTransition.onEnd {
            b.buttonShare.showAnimated()
            b.buttonNfc.showAnimated(DURATION_SHORT / 3)
            b.buttonEdit.showAnimated(2 * DURATION_SHORT / 3)
            b.buttonRemove.showAnimated(DURATION_SHORT)
        }

        viewModelFacade.onShareCard()
                .subscribe { openShareChooser(R.string.share_card, it) }
                .addTo(disposables)

        viewModelFacade.onShareCardUsingNfc()
                .subscribe { shareUsingNfc(R.string.share_card, it) }
                .addTo(disposables)
    }

}
