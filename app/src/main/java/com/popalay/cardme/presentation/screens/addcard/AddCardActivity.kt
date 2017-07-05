package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import com.popalay.cardme.App
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddCardBinding
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.CustomFactory
import ru.terrakok.cicerone.android.SupportAppNavigator
import javax.inject.Inject

class AddCardActivity : BaseActivity() {

    @Inject lateinit var factory: CustomFactory

    private lateinit var b: ActivityAddCardBinding

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

    private val navigator = object : SupportAppNavigator(this, 0) {
        override fun createFragment(screenKey: String, data: Any): Fragment? {
            return null
        }

        override fun createActivityIntent(screenKey: String, data: Any): Intent? {
            return null
        }
    }

    /*TODO
    @Override
    public void showError(String message) {
        final Dialog errorDialog = DialogFactory.createCustomButtonsDialog(this, message,
                getString(R.string.action_close), null,
                (dialog, which) -> presenter.onCloseClick(), dialog -> presenter.onErrorDialogDismiss());

        errorDialog.setCancelable(false);
        errorDialog.show();
    }*/

    private fun initUI() {
        setSupportActionBar(b.toolbar)
    }

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, AddCardActivity::class.java)
        }
    }
}
