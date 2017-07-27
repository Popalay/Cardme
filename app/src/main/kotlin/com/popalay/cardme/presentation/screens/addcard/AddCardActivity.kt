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
import javax.inject.Inject

class AddCardActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityAddCardBinding

    companion object {
        const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"

        fun getIntent(context: Context, cardNumber: String): Intent {
            val intent = Intent(context, AddCardActivity::class.java)
            intent.putExtra(KEY_CARD_NUMBER, cardNumber)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityAddCardBinding>(this, R.layout.activity_add_card)
        b.vm = ViewModelProviders.of(this, factory).get(AddCardViewModel::class.java)
        initUI()
    }

    private fun initUI() {
        setSupportActionBar(b.toolbar)
    }
}
