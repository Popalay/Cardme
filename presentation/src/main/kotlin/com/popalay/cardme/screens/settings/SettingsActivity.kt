package com.popalay.cardme.screens.settings

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivitySettingsBinding
import com.popalay.cardme.base.RightSlidingActivity
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import javax.inject.Inject

class SettingsActivity : RightSlidingActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding(R.layout.activity_settings)
        b.vm = getViewModel<SettingsViewModel>(factory)
        initUI()
    }

    override fun getRootView(): View = b.root

    private fun initUI() {
        setSupportActionBar(b.toolbar)
    }

}
