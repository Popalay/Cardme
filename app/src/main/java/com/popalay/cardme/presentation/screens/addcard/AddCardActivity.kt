package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.App
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddCardBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomFactory
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.DialogFactory
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class AddCardActivity : BaseActivity() {

    @Inject lateinit var factory: CustomFactory

    private lateinit var b: ActivityAddCardBinding
    private lateinit var viewModelFacade: AddCardViewModelFacade

    companion object {
        fun getIntent(context: Context) = Intent(context, AddCardActivity::class.java)
    }

    private val navigator = object : CustomNavigator(this) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        b = DataBindingUtil.setContentView<ActivityAddCardBinding>(this, R.layout.activity_add_card)
        ViewModelProviders.of(this, factory).get(AddCardViewModel::class.java).let {
            viewModelFacade = it
            b.vm = it
        }
        initUI()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        App.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.getNavigatorHolder().removeNavigator()
    }

    private fun initUI() {
        setSupportActionBar(b.toolbar)

        viewModelFacade.doOnShowCardExistsDialog {
            DialogFactory.createCustomButtonsDialog(this@AddCardActivity,
                    R.string.error_card_exist, R.string.action_close,
                    onDismiss = viewModelFacade::onShowCardExistsDialogDismiss)
                    .apply { setCancelable(false) }
        }.addTo(disposables)
    }
}
