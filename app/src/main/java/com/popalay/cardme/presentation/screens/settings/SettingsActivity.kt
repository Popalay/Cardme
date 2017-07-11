package com.popalay.cardme.presentation.screens.settings

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivitySettingsBinding
import com.popalay.cardme.presentation.base.BaseActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivitySettingsBinding

    companion object {

        fun getIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        ViewModelProviders.of(this, factory).get(SettingsViewModel::class.java).let {
            b.vm = it
        }
        initUI()
    }


    private fun initUI() {
        setSupportActionBar(b.toolbar)
    }

}
