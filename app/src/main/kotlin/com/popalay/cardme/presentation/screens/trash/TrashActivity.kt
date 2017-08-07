package com.popalay.cardme.presentation.screens.trash

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.popalay.cardme.R
import com.popalay.cardme.databinding.ActivityTrashBinding
import com.popalay.cardme.presentation.base.RightSlidingActivity
import com.popalay.cardme.utils.recycler.SpacingItemDecoration
import javax.inject.Inject

class TrashActivity : RightSlidingActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, TrashActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityTrashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityTrashBinding>(this, R.layout.activity_trash)
        b.vm = ViewModelProviders.of(this, factory).get(TrashViewModel::class.java)
        initUI()
    }

    override fun getRootView(): View = b.root

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.trash_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_empty_trash -> b.vm?.emptyTrashClick?.accept(true)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initUI() {
        setSupportActionBar(b.toolbar)

        b.listCards.addItemDecoration(SpacingItemDecoration.create {
            dividerSize = resources.getDimension(R.dimen.normal).toInt()
            showBetween = true
            showOnSides = true
        })
    }

}