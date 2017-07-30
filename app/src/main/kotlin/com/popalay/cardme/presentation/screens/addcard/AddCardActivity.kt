package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityAddCardBinding
import com.popalay.cardme.presentation.base.RightSlidingActivity
import javax.inject.Inject

class AddCardActivity : RightSlidingActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityAddCardBinding

    companion object {

        fun getIntent(context: Context) = Intent(context, AddCardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityAddCardBinding>(this, R.layout.activity_add_card)
        b.vm = ViewModelProviders.of(this, factory).get(AddCardViewModel::class.java)
        initUI()
    }

    override fun getRootView(): View = b.root

    private fun initUI() {
        setSupportActionBar(b.toolbar)
    }
}
