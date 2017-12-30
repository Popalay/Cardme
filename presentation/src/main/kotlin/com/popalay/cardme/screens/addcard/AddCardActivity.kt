package com.popalay.cardme.screens.addcard

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import com.popalay.cardme.R
import com.popalay.cardme.base.RightSlidingActivity
import com.popalay.cardme.base.mvi.MviView
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.getViewModel
import io.reactivex.Observable
import javax.inject.Inject

class AddCardActivity : RightSlidingActivity(), MviView<AddCardViewState, AddCardIntent> {

    companion object {

        const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"
        fun getIntent(context: Context, card: Card) = Intent(context, AddCardActivity::class.java).apply {
            putExtra(KEY_CARD_NUMBER, card.number)
        }
    }

    private val layoutRoot: ViewGroup by bindView(R.id.layout_root)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)
    private val appBarLayout: AppBarLayout by bindView(R.id.app_bar_layout)
    private val textHolder: AutoCompleteTextView by bindView(R.id.text_holder)
    private val textTitle: TextInputEditText by bindView(R.id.text_title)

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var acceptMenuItem: MenuItem

    private val viewModel by lazy { getViewModel<AddCardViewModel>(factory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initUI()

        viewModel.processIntents(intents())
        viewModel.states()
                .bindToLifecycle()
                .subscribe(::render)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.accept_menu, menu)
        acceptMenuItem = menu.findItem(R.id.action_accept)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        //R.id.action_accept -> viewModelFacade.onAcceptClick()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getRootView(): View = layoutRoot

    override fun intents(): Observable<AddCardIntent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(state: AddCardViewState) {

    }

    private fun initUI() {
        setSupportActionBar(toolbar)

        textTitle.setOnFocusChangeListener { _, focus ->
            appBarLayout.setExpanded(!focus)
        }

        textHolder.setOnFocusChangeListener { _, focus ->
            appBarLayout.setExpanded(!focus)
        }
    }
}
