package com.popalay.cardme.screens.addcard

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.DEBOUNCE_DELAY_MS
import com.popalay.cardme.R
import com.popalay.cardme.base.RightSlidingActivity
import com.popalay.cardme.base.mvi.MviView
import com.popalay.cardme.screens.stringAdapter
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.setTextIfNeeded
import com.popalay.cardme.widget.CreditCardView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddCardActivity : RightSlidingActivity(), MviView<AddCardViewState, AddCardIntent> {

    companion object {

        private const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"

        fun getIntent(context: Context, number: String) = Intent(context, AddCardActivity::class.java).apply {
            putExtra(KEY_CARD_NUMBER, number)
        }
    }

    private val layoutRoot: ViewGroup by bindView(R.id.layout_root)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val appBarLayout: AppBarLayout by bindView(R.id.app_bar_layout)
    private val textHolder: TextView by bindView(R.id.text_holder)
    private val textTitle: TextView by bindView(R.id.text_title)
    private val textCardNumber: TextView by bindView(R.id.text_card_number)
    private val viewCreditCard: CreditCardView by bindView(R.id.view_credit_card)
    private val inputHolder: AutoCompleteTextView by bindView(R.id.input_holder)
    private val inputTitle: TextInputEditText by bindView(R.id.input_title)

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: AddCardViewModel
    private lateinit var lastState: AddCardViewState

    private var acceptMenuItem: MenuItem? = null
    private val extraCardNumber get() = intent.getStringExtra(KEY_CARD_NUMBER)
    private val acceptIntentPublisher = PublishSubject.create<AddCardIntent.Accept>()

    override val intents: Observable<AddCardIntent>
        get() = Observable.merge(
                getInitialIntent(),
                getTitleChangedIntent(),
                getNameChangedIntent(),
                getAcceptIntent()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initUI()

        viewModel = getViewModel<AddCardViewModel>(factory).also { bind(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.accept_menu, menu)
        acceptMenuItem = menu.findItem(R.id.action_accept)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_accept) acceptIntentPublisher.onNext(AddCardIntent.Accept(lastState.card))
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun getRootView(): View = layoutRoot

    override fun accept(state: AddCardViewState) {
        lastState = state
        with(state) {
            Log.w("AddCardState", error)
            with(card) {
                inputTitle.setTextIfNeeded(title)
                inputHolder.setTextIfNeeded(holderName)
                textTitle.setTextIfNeeded(title)
                textHolder.setTextIfNeeded(holderName)
                imageCardType.setImageResource(iconRes)
                textCardNumber.text = number
                viewCreditCard.seed = generatedBackgroundSeed
            }
            inputHolder.stringAdapter(holderNames)
            viewCreditCard.isWithImage = showBackground
            acceptMenuItem?.isEnabled = canSave
        }
    }

    private fun bind(viewModel: AddCardViewModel) {
        viewModel.states
                .bindToLifecycle()
                .subscribe(::accept)
        viewModel.processIntents(intents)
    }

    private fun getInitialIntent() = Observable.fromArray(
            AddCardIntent.Initial.GetCard(extraCardNumber),
            AddCardIntent.Initial.GetHolderNames,
            AddCardIntent.Initial.GetShouldShowBackground
    )

    private fun getNameChangedIntent() = RxTextView.afterTextChangeEvents(inputHolder)
            .skipInitialValue()
            .throttleLast(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
            .map { it.editable().toString() }
            .distinctUntilChanged()
            .map { lastState.card.copy(holderName = it) }
            .map(AddCardIntent::CardNameChanged)

    private fun getTitleChangedIntent() = RxTextView.afterTextChangeEvents(inputTitle)
            .skipInitialValue()
            .throttleLast(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS)
            .map { it.editable().toString() }
            .distinctUntilChanged()
            .map { lastState.card.copy(title = it) }
            .map(AddCardIntent::CardTitleChanged)

    private fun getAcceptIntent() = acceptIntentPublisher
            .applyThrottling()

    private fun initUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Observable.merge(RxView.focusChanges(inputTitle), RxView.focusChanges(inputHolder))
                .map(Boolean::not)
                .bindToLifecycle()
                .subscribe(appBarLayout::setExpanded)
    }
}