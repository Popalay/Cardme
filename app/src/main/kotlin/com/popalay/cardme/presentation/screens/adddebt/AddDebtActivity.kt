package com.popalay.cardme.presentation.screens.adddebt

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddDebtBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.extensions.*
import com.popalay.cardme.utils.transitions.FabTransform
import javax.inject.Inject


class AddDebtActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, AddDebtActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject override lateinit var navigator: CustomNavigator

    private lateinit var b: ActivityAddDebtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding(R.layout.activity_add_debt)
        b.vm = getViewModel<AddDebtViewModel>(factory)
        initUi()
    }

    override fun onBackPressed() = exitWithAnimation()

    fun exitWithAnimation() {
        b.buttonSave.hideAnimated { supportFinishAfterTransition() }
    }

    private fun initUi() {
        FabTransform.setup(this, b.container)
        window.sharedElementEnterTransition.onEnd { b.buttonSave.showAnimated() }
    }
}
