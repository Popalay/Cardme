package com.popalay.cardme.screens.carddetails

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.DEBOUNCE_DELAY_MS
import com.popalay.cardme.DURATION_SHORT
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseActivity
import com.popalay.cardme.base.mvi.MviView
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.screens.setVisibility
import com.popalay.cardme.screens.stringAdapter
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.openShareChooser
import com.popalay.cardme.utils.extensions.setTextIfNeeded
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.widget.CharacterWrapTextView
import com.popalay.cardme.widget.CreditCardView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CardDetailsActivity : BaseActivity(), MviView<CardDetailsViewState, CardDetailsIntent>,
    NfcAdapter.CreateNdefMessageCallback {

    companion object {

        internal const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"

        internal fun getIntent(context: Context, number: String) =
            Intent(context, CardDetailsActivity::class.java).apply {
                putExtra(KEY_CARD_NUMBER, number)
            }
    }

    private val layoutRoot: ViewGroup by bindView(R.id.layout_root)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textNumber: CharacterWrapTextView by bindView(R.id.text_number)
    private val inputTitle: EditText by bindView(R.id.input_title)
    private val inputHolder: AutoCompleteTextView by bindView(R.id.input_holder)
    private val cardView: CreditCardView by bindView(R.id.card_view)
    private val buttonShare: ImageButton by bindView(R.id.button_share)
    private val buttonNfc: ImageButton by bindView(R.id.button_nfc)
    private val buttonEdit: ImageButton by bindView(R.id.button_edit)
    private val buttonRemove: ImageButton by bindView(R.id.button_remove)

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject override lateinit var navigator: CustomNavigator
    @Inject lateinit var router: CustomRouter

    private lateinit var viewModel: CardDetailsViewModel
    private var lastState: CardDetailsViewState? = null
    private val extraCardNumber get() = requireNotNull(intent.getStringExtra(KEY_CARD_NUMBER))

    override val intents: Observable<CardDetailsIntent>
        get() = Observable.merge(
            listOf(
                getInitialIntent(),
                getNameChangedIntent(),
                getTitleChangedIntent(),
                getEditIntent(),
                getMarkAsTrashIntent()
            )
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        NfcAdapter.getDefaultAdapter(this)?.setNdefPushMessageCallback(this, this)
        initUi()

        viewModel = getViewModel<CardDetailsViewModel>(factory).also { bind(it) }
    }

    override fun accept(state: CardDetailsViewState) {
        if (lastState == state) return

        //TODO: the state is interrupting the animation
        with(state) {
            Log.w("AddCardState", error)
            if (animateButtons) {
                buttonRemove.showAnimated()
                buttonEdit.showAnimated(DURATION_SHORT / 3)
                buttonNfc.showAnimated(2 * DURATION_SHORT / 3)
                buttonShare.showAnimated(DURATION_SHORT)
            }
            buttonNfc.setVisibility(!card.isPending && nfcEnabled && !inEditMode)
            buttonShare.setVisibility(!card.isPending && !inEditMode)
            buttonRemove.setVisibility(!card.isPending && !inEditMode)
            inputHolder.stringAdapter(holderNames)
            cardView.isWithImage = showBackground
            inputTitle.setTextIfNeeded(card.title)
            inputHolder.setTextIfNeeded(card.holderName)
            imageCardType.setImageResource(card.iconRes)
            textNumber.text = card.number
            cardView.seed = card.generatedBackgroundSeed

            inputHolder.isEnabled = inEditMode
            inputTitle.isEnabled = inEditMode
            buttonEdit.isEnabled = !inEditMode || inEditMode && canSave
            buttonEdit.setImageResource(if (inEditMode) R.drawable.ic_done else R.drawable.ic_write)
        }
        lastState = state
    }

    override fun createNdefMessage(event: NfcEvent?): Nothing? = null
    //createNdefMessage(viewModelFacade.getCardShareNfcObject().blockingGet().toByteArray())

    override fun onBackPressed() {
        exitWithAnimation()
    }

    internal fun exitWithAnimation() {
        buttonRemove.hideAnimated()
        buttonEdit.hideAnimated(DURATION_SHORT / 3)
        buttonNfc.hideAnimated(2 * DURATION_SHORT / 3)
        buttonShare.hideAnimated(DURATION_SHORT) { supportFinishAfterTransition() }
    }

    private fun bind(viewModel: CardDetailsViewModel) {
        viewModel.states
            .bindToLifecycle()
            .subscribe(::accept)
        viewModel.processIntents(intents)
    }

    private fun getInitialIntent() = Observable.fromArray(
        CardDetailsIntent.Initial.GetCard(extraCardNumber),
        CardDetailsIntent.Initial.GetHolderNames,
        CardDetailsIntent.Initial.GetShouldShowBackground
    )

    private fun getNameChangedIntent() = RxTextView.afterTextChangeEvents(inputHolder)
        .skipInitialValue()
        .throttleLast(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS, Schedulers.computation())
        .map { it.editable().toString() }
        .distinctUntilChanged()
        .filter { lastState != null }
        .map { lastState!!.card.copy(holderName = it) }
        .map(CardDetailsIntent::CardNameChanged)

    private fun getTitleChangedIntent() = RxTextView.afterTextChangeEvents(inputTitle)
        .skipInitialValue()
        .throttleLast(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS, Schedulers.computation())
        .map { it.editable().toString() }
        .distinctUntilChanged()
        .filter { lastState != null }
        .map { lastState!!.card.copy(title = it) }
        .map(CardDetailsIntent::CardTitleChanged)

    private fun getEditIntent() = RxView.clicks(buttonEdit)
        .throttleLast(DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS, Schedulers.computation())
        .filter { lastState != null }
        .map { lastState!! }
        .map { CardDetailsIntent.Edit(it.card, it.inEditMode) }

    private fun getMarkAsTrashIntent() = RxView.clicks(buttonRemove)
        .applyThrottling()
        .filter { lastState != null }
        .map { lastState!! }
        .map { it.card }
        .map(CardDetailsIntent::MarkAsTrash)

    private fun initUi() {
        layoutRoot.setOnClickListener { router.exit() }

        buttonShare.setOnClickListener {
            openShareChooser(R.string.share_card, lastState?.card?.number ?: "")
        }
/*
        viewModelFacade.onShareCardUsingNfc()
                .bindToLifecycle()
                .subscribe { shareUsingNfc(R.string.share_card, it) }*/
    }
}