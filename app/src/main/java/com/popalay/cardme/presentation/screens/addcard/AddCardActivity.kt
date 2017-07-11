package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddCardBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.DialogFactory
import dagger.android.AndroidInjection
import io.card.payment.CreditCard
import io.reactivex.rxkotlin.addTo
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

class AddCardActivity : BaseActivity() {

    @Inject lateinit var navigationHolder: NavigatorHolder
    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityAddCardBinding
    private lateinit var viewModelFacade: AddCardViewModelFacade

    companion object {
        val KEY_CREDIT_CARD = "KEY_CREDIT_CARD"

        fun getIntent(context: Context, creditCard: CreditCard): Intent {
            val intent = Intent(context, AddCardActivity::class.java)
            intent.putExtra(KEY_CREDIT_CARD, creditCard)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityAddCardBinding>(this, R.layout.activity_add_card)
        ViewModelProviders.of(this, factory).get(AddCardViewModel::class.java).let {
            b.vm = it
            viewModelFacade = it
        }
        initUI()
    }

    private fun initUI() {
        setSupportActionBar(b.toolbar)

        //TODO
        viewModelFacade.doOnShowCardExistsDialog {
            DialogFactory.createCustomButtonsDialog(this@AddCardActivity,
                    R.string.error_card_exist, R.string.action_close,
                    onDismiss = viewModelFacade::onShowCardExistsDialogDismiss)
                    .apply { setCancelable(false) }
        }.addTo(disposables)
    }
}
