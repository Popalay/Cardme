package com.popalay.cardme.presentation.screens.addcard

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
import com.popalay.cardme.databinding.ActivityAddCardBinding
import com.popalay.cardme.presentation.base.RightSlidingActivity
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class AddCardActivity : RightSlidingActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, AddCardActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var b: ActivityAddCardBinding
    private lateinit var viewModelFacade: AddCardViewModelFacade
    private lateinit var acceptMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView<ActivityAddCardBinding>(this, R.layout.activity_add_card)
        ViewModelProviders.of(this, factory).get(AddCardViewModel::class.java).let {
            b.vm = it
            viewModelFacade = it
        }
        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.accept_menu, menu)
        acceptMenuItem = menu.findItem(R.id.action_accept)

        viewModelFacade.onCanSaveStateChanged()
                .subscribe { acceptMenuItem.isEnabled = it }
                .addTo(disposables)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_accept -> viewModelFacade.onAcceptClick()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getRootView(): View = b.root

    private fun initUI() {
        setSupportActionBar(b.toolbar)

        b.textTitle.setOnFocusChangeListener { _, focus ->
            b.appBarLayout.setExpanded(!focus)
        }

        b.textHolder.setOnFocusChangeListener { _, focus ->
            b.appBarLayout.setExpanded(!focus)
        }

    }
}
