package com.popalay.cardme.screens.carddetails

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
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
import com.popalay.cardme.utils.extensions.onEnd
import com.popalay.cardme.utils.extensions.openShareChooser
import com.popalay.cardme.utils.extensions.setTextIfNeeded
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.widget.CharacterWrapTextView
import com.popalay.cardme.widget.CreditCardView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CardDetailsActivity : BaseActivity(), MviView<CardDetailsViewState, CardDetailsIntent>, NfcAdapter.CreateNdefMessageCallback {

    companion object {

        internal const val KEY_CARD_NUMBER = "KEY_CARD_NUMBER"

        internal fun getIntent(context: Context, number: String) = Intent(context, CardDetailsActivity::class.java).apply {
            putExtra(KEY_CARD_NUMBER, number)
        }
    }

    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textNumber: CharacterWrapTextView by bindView(R.id.text_number)
    private val inputTitle: EditText by bindView(R.id.input_title)
    private val inputHolder: AutoCompleteTextView by bindView(R.id.input_holder)
    private val cardView: CreditCardView by bindView(R.id.card_view)
    private val buttonShare: ImageButton by bindView(R.id.button_share)
    private val buttonNfc: ImageButton by bindView(R.id.button_nfc)
    private val buttonEdit: ImageButton by bindView(R.id.button_edit)
    private val buttonRemove: ImageButton by bindView(R.id.button_remove)
    private val layoutRoot: ConstraintLayout by bindView(R.id.layout_root)

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject override lateinit var navigator: CustomNavigator
    @Inject lateinit var router: CustomRouter

    private lateinit var viewModel: CardDetailsViewModel
    private var lastState: CardDetailsViewState? = null
    private val extraCardNumber get() = requireNotNull(intent.getStringExtra(KEY_CARD_NUMBER))

    override val intents: Observable<CardDetailsIntent>
        get() = Observable.merge(listOf(
                getInitialIntent(),
                getNameChangedIntent(),
                getTitleChangedIntent(),
                getEditIntent(),
                getMarkAsTrashIntent()
        ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        NfcAdapter.getDefaultAdapter(this)?.setNdefPushMessageCallback(this, this)
        initUi()

        viewModel = getViewModel<CardDetailsViewModel>(factory).also { bind(it) }
    }

    override fun accept(state: CardDetailsViewState) {
        if (lastState == state) return
        with(state) {
            Log.w("AddCardState", error)
            inputHolder.stringAdapter(holderNames)
            cardView.isWithImage = showBackground
            buttonNfc.setVisibility(nfcEnabled && !inEditMode)
            if (lastState?.inEditMode != state.inEditMode) toggleEditMode()
            with(card) {
                inputTitle.setTextIfNeeded(title)
                inputHolder.setTextIfNeeded(holderName)
                imageCardType.setImageResource(iconRes)
                textNumber.text = number
                cardView.seed = generatedBackgroundSeed
                buttonShare.setVisibility(!isPending && !inEditMode)
                buttonRemove.setVisibility(!isPending && !inEditMode)
            }
        }
        lastState = state
    }

    override fun createNdefMessage(event: NfcEvent?) = null
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

    private fun CardDetailsViewState.toggleEditMode() {
        if (lastState != null) {
            if (inEditMode) {
                if (!card.isPending) buttonShare.hideAnimated()
                if (!card.isPending) buttonRemove.hideAnimated()
                if (nfcEnabled) buttonNfc.hideAnimated()
            } else {
                if (!card.isPending) buttonShare.showAnimated()
                if (!card.isPending) buttonRemove.showAnimated()
                if (nfcEnabled) buttonNfc.showAnimated()
            }
        }
        inputHolder.isEnabled = inEditMode
        inputTitle.isEnabled = inEditMode
        buttonEdit.isEnabled = !inEditMode || inEditMode && canSave
        buttonEdit.setImageResource(if (inEditMode) R.drawable.ic_done else R.drawable.ic_write)
    }

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

    /*   private fun getAcceptIntent() = RxView.clicks(buttonSave)
               .applyThrottling()
               .map { lastState.debt }
               .map(AddDebtIntent::Accept)*/

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
        window.sharedElementEnterTransition.onEnd {
            listOf(buttonRemove, buttonEdit, buttonNfc, buttonShare)
                    .reversed()
                    .forEachIndexed { index, view -> view.showAnimated(index * DURATION_SHORT / 3) }
        }

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