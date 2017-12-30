package com.popalay.cardme.screens.addcard

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.R
import com.popalay.cardme.base.RightSlidingActivity
import com.popalay.cardme.base.mvi.MviView
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.screens.stringAdapter
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.toObservable
import com.popalay.cardme.widget.CreditCardView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddCardActivity : RightSlidingActivity(), MviView<AddCardViewState, AddCardIntent> {

    companion object {

        private const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"

        fun getIntent(context: Context, card: Card) = Intent(context, AddCardActivity::class.java).apply {
            putExtra(KEY_CARD_NUMBER, card.number)
        }
    }

    private val layoutRoot: ViewGroup by bindView(R.id.layout_root)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val appBarLayout: AppBarLayout by bindView(R.id.app_bar_layout)
    private val textHolder: AutoCompleteTextView by bindView(R.id.text_holder)
    private val textTitle: TextView by bindView(R.id.text_title)
    private val textCardNumber: TextView by bindView(R.id.text_card_number)
    private val viewCreditCard: CreditCardView by bindView(R.id.view_credit_card)
    private val inputHolder: AutoCompleteTextView by bindView(R.id.input_holder)
    private val inputTitle: TextInputEditText by bindView(R.id.input_title)

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var acceptMenuItem: MenuItem

    private val viewModel by lazy { getViewModel<AddCardViewModel>(factory) }
    private val extraCardNumber get() = intent.getStringExtra(KEY_CARD_NUMBER)
    private val acceptIntentPublisher = PublishSubject.create<AddCardIntent.Accept>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initUI()

        viewModel.processIntents(intents())
        viewModel.states()
                .bindToLifecycle()
                .subscribe(::accept)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.accept_menu, menu)
        acceptMenuItem = menu.findItem(R.id.action_accept)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_accept -> acceptIntentPublisher.onNext(AddCardIntent.Accept)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun getRootView(): View = layoutRoot

    override fun intents(): Observable<AddCardIntent> = Observable.merge(
            AddCardIntent.Initial(extraCardNumber).toObservable(),
            RxTextView.textChanges(inputTitle).map(CharSequence::toString).map(AddCardIntent::TitleChanged),
            RxTextView.textChanges(inputHolder).map(CharSequence::toString).map(AddCardIntent::NameChanged),
            acceptIntentPublisher
    )

    override fun accept(state: AddCardViewState) {
        with(state) {
            with(card) {
                inputTitle.setText(title)
                textHolder.setText(holderName)
                inputHolder.setText(holderName)
                imageCardType.setImageResource(iconRes)
                textTitle.text = title
                textCardNumber.text = number
                viewCreditCard.seed = generatedBackgroundSeed
            }
            inputHolder.stringAdapter(holderNames)
            viewCreditCard.isWithImage = showBackground
            acceptMenuItem.isEnabled = canSave
        }
    }

    private fun initUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textTitle.setOnFocusChangeListener { _, focus ->
            appBarLayout.setExpanded(!focus)
        }

        textHolder.setOnFocusChangeListener { _, focus ->
            appBarLayout.setExpanded(!focus)
        }
    }
}