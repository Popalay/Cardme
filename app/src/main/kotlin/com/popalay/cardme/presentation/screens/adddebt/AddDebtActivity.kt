package com.popalay.cardme.presentation.screens.adddebt

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddDebtBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.onEnd
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.utils.transitions.FabTransform
import javax.inject.Inject


class AddDebtActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityAddDebtBinding

    override var navigator = object : CustomNavigator(this) {
        override fun exit() = exitWithAnimation()
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, AddDebtActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityAddDebtBinding>(this, R.layout.activity_add_debt)
        FabTransform.setup(this, b.container)
        b.vm = ViewModelProviders.of(this, factory).get(AddDebtViewModel::class.java)
        initUi()
    }

    override fun onBackPressed() = exitWithAnimation()

    private fun exitWithAnimation() {
        b.buttonSave.hideAnimated { supportFinishAfterTransition() }
    }

    private fun initUi() {
        window.sharedElementEnterTransition.onEnd {
            b.buttonSave.showAnimated()
        }
    }
}
