package com.popalay.cardme.screens.addcard

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
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
import com.popalay.cardme.R
import com.popalay.cardme.base.RightSlidingActivity
import com.popalay.cardme.base.ViewModelFactory
import com.popalay.cardme.base.mvi.BindableMviView
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.screens.setDropDownItems
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.setTextIfNeeded
import com.popalay.cardme.widget.CreditCardView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.reflect.KProperty0

class AddCardActivity : RightSlidingActivity(), BindableMviView<AddCardViewState, AddCardIntent> {

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
    private val textHolder: TextView by bindView(R.id.text_holder)
    private val textTitle: TextView by bindView(R.id.text_title)
    private val textCardNumber: TextView by bindView(R.id.text_card_number)
    private val viewCreditCard: CreditCardView by bindView(R.id.view_credit_card)
    private val inputHolder: AutoCompleteTextView by bindView(R.id.input_holder)
    private val inputTitle: TextInputEditText by bindView(R.id.input_title)

    private val extraCardNumber get() = intent.getStringExtra(KEY_CARD_NUMBER)
    @Inject lateinit var factory: ViewModelProvider.Factory
    private var acceptMenuItem: MenuItem? = null

    private lateinit var lastState: AddCardViewState

    private val intentsPublisher = PublishSubject.create<AddCardIntent>()

    private val initialIntents
        get() = Observable.fromArray(
            AddCardIntent.Initial.GetCard(extraCardNumber),
            AddCardIntent.Initial.GetShouldShowBackground
        )

    private val nameChangedIntent
        get() = RxTextView.textChanges(inputHolder)
            .skipInitialValue()
            .subscribeOn(AndroidSchedulers.mainThread())
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .filter { lastState.card != null }
            .map { lastState.card?.copy(holderName = it) }
            .map(AddCardIntent::CardNameChanged)

    private val titleChangedIntent
        get() = RxTextView.textChanges(inputTitle)
            .skipInitialValue()
            .subscribeOn(AndroidSchedulers.mainThread())
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .filter { lastState.card != null }
            .map { lastState.card?.copy(title = it) }
            .map(AddCardIntent::CardTitleChanged)

    override val intents: Observable<AddCardIntent> = Observable.defer {
        Observable.merge(
            initialIntents,
            titleChangedIntent,
            nameChangedIntent,
            intentsPublisher
        )
    }

    override fun accept(state: AddCardViewState) {
        Log.v("STATE", state.toString())
        lastState = state
        with(state) {
            if (card != null) with(card) {
                inputTitle.setTextIfNeeded(title)
                inputHolder.setTextIfNeeded(holderName)
                textTitle.text = title
                textHolder.text = holderName
                imageCardType.setImageResource(iconRes)
                textCardNumber.text = number
                viewCreditCard.seed = generatedBackgroundSeed
            }

            viewCreditCard.isWithImage = showBackground
            acceptMenuItem?.isEnabled = canSave
            inputHolder.setDropDownItems(holderNames)
            if (shouldShowSuggestions && inputHolder.isFocused) inputHolder.showDropDown() else inputHolder.dismissDropDown()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initUI()

        bind(getViewModel<AddCardViewModel>(factory))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.accept_menu, menu)
        acceptMenuItem = menu.findItem(R.id.action_accept)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_accept) intentsPublisher.onNext(AddCardIntent.Accept(lastState.card!!))
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun getRootView(): View = layoutRoot

    private fun initUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Observable.merge(RxView.focusChanges(inputTitle), RxView.focusChanges(inputHolder))
            .map(Boolean::not)
            .bindToLifecycle()
            .subscribe(appBarLayout::setExpanded)
    }
}