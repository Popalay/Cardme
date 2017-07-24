package com.popalay.cardme.presentation.screens.trash

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityTrashBinding
import com.popalay.cardme.presentation.base.BaseActivity
import javax.inject.Inject

class TrashActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, TrashActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityTrashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityTrashBinding>(this, R.layout.activity_trash)
        b.vm = ViewModelProviders.of(this, factory).get(TrashViewModel::class.java)
    }

}