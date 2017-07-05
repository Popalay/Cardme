package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.App
import com.popalay.cardme.R
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.databinding.ActivityAddCardBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomFactory
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.DialogFactory
import javax.inject.Inject

class AddCardActivity : BaseActivity() {

    @Inject lateinit var factory: CustomFactory

    private lateinit var b: ActivityAddCardBinding

    companion object {
        fun getIntent(context: Context) = Intent(context, AddCardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        b = DataBindingUtil.setContentView<ActivityAddCardBinding>(this, R.layout.activity_add_card)
        val vm = ViewModelProviders.of(this, factory).get(AddCardViewModel::class.java)
        b.vm = vm
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
    }

    private val navigator = object : CustomNavigator(this) {

        override fun showError(exception: AppException) {
            DialogFactory.createCustomButtonsDialog(this@AddCardActivity,
                    exception.messageRes, R.string.action_close, 0,
                    { _, _ -> exit() }) { }
                    .apply {
                        setCancelable(false)
                    }.show()
        }
    }
}
