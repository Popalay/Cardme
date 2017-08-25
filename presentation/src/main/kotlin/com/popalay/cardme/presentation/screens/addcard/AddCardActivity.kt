package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.popalay.cardme.R
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.databinding.ActivityAddCardBinding
import com.popalay.cardme.presentation.base.RightSlidingActivity
import com.popalay.cardme.utils.extensions.getDataBinding
import com.popalay.cardme.utils.extensions.getViewModel
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class AddCardActivity : RightSlidingActivity() {

    companion object {
        const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"
        const val KEY_FORMATTED_CARD_NUMBER = "KEY_FORMATTED_CARD_NUMBER"
        fun getIntent(context: Context, card: Card) = Intent(context, AddCardActivity::class.java).apply {
            putExtra(KEY_CARD_NUMBER, card.number)
            putExtra(KEY_FORMATTED_CARD_NUMBER, card.redactedNumber)
        }
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var viewModelFacade: AddCardViewModelFacade

    private lateinit var b: ActivityAddCardBinding
    private lateinit var acceptMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding(R.layout.activity_add_card)
        b.vm = getViewModel<AddCardViewModel>(factory)
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
