package com.popalay.cardme.screens.carddetails

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.DURATION_SHORT
import com.popalay.cardme.R
import com.popalay.cardme.base.BaseActivity
import com.popalay.cardme.base.mvi.MviView
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.screens.SCREEN_ADD_CARD
import com.popalay.cardme.screens.carddetails.CardDetailsIntent.EnterTransitionFinished
import com.popalay.cardme.screens.setVisibility
import com.popalay.cardme.utils.animation.SimpleTransitionListener
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.bindView
import com.popalay.cardme.utils.extensions.extra
import com.popalay.cardme.utils.extensions.getViewModel
import com.popalay.cardme.utils.extensions.hideAnimated
import com.popalay.cardme.utils.extensions.openShareChooser
import com.popalay.cardme.utils.extensions.setTextIfNeeded
import com.popalay.cardme.utils.extensions.showAnimated
import com.popalay.cardme.utils.extensions.subscribeBy
import com.popalay.cardme.widget.CharacterWrapTextView
import com.popalay.cardme.widget.CreditCardView
import io.reactivex.Observable
import javax.inject.Inject

class CardDetailsActivity : BaseActivity(), MviView<CardDetailsViewState, CardDetailsIntent> {

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
	private val textTitle: TextView by bindView(R.id.text_title)
	private val textHolder: TextView by bindView(R.id.text_holder)
	private val cardView: CreditCardView by bindView(R.id.card_view)
	private val buttonShare: ImageButton by bindView(R.id.button_share)
	private val buttonNfc: ImageButton by bindView(R.id.button_nfc)
	private val buttonEdit: ImageButton by bindView(R.id.button_edit)
	private val buttonRemove: ImageButton by bindView(R.id.button_remove)

	@Inject lateinit var factory: ViewModelProvider.Factory
	@Inject override lateinit var navigator: CustomNavigator
	@Inject lateinit var router: CustomRouter

	private val viewModel by lazy { getViewModel<CardDetailsViewModel>(factory) }
	private val extraCardNumber: String by extra(KEY_CARD_NUMBER)
	private var lastState: CardDetailsViewState = CardDetailsViewState.idle()

	override val intents: Observable<CardDetailsIntent>
		get() = Observable.merge(
			listOf(
				getInitialIntent(),
				getMarkAsTrashIntent(),
				getEnterTransitionFinishedIntent(),
				getShareByNfcIntent()
			)
		)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_card_details)
		initUi()
		bind(viewModel)
	}

	override fun accept(state: CardDetailsViewState) {
		if (lastState == state) return
		with(state) {
			Log.w("AddCardState", error)
			buttonNfc.setVisibility(nfcEnabled)
			cardView.isWithImage = showBackground
			textTitle.setTextIfNeeded(card.title)
			textHolder.setTextIfNeeded(card.holderName)
			imageCardType.setImageResource(card.iconRes)
			textNumber.text = card.number
			cardView.seed = card.generatedBackgroundSeed

			if (animateButtons) {
				buttonRemove.showAnimated()
				buttonEdit.showAnimated(DURATION_SHORT / 3)
				buttonNfc.showAnimated(2 * DURATION_SHORT / 3)
				buttonShare.showAnimated(DURATION_SHORT)
			}
		}
		lastState = state
	}

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
		CardDetailsIntent.Initial.GetShouldShowBackground
	)

	private fun getMarkAsTrashIntent() = RxView.clicks(buttonRemove)
		.applyThrottling()
		.map { lastState.card }
		.map(CardDetailsIntent::MarkAsTrash)

	private fun getShareByNfcIntent() = RxView.clicks(buttonNfc)
		.applyThrottling()
		.map { lastState.card }
		.map(CardDetailsIntent::ShareByNfc)

	private fun getEnterTransitionFinishedIntent() = Observable.create<EnterTransitionFinished> {
		window.enterTransition.addListener(object : SimpleTransitionListener {
			override fun onTransitionEnd(transition: Transition?) {
				it.onNext(CardDetailsIntent.EnterTransitionFinished)
				it.onComplete()
				it.setCancellable { transition?.removeListener(this) }
			}
		})
	}

	private fun initUi() {
		RxView.clicks(layoutRoot)
			.applyThrottling()
			.bindToLifecycle()
			.subscribeBy { router.exit() }

		RxView.clicks(buttonShare)
			.applyThrottling()
			.bindToLifecycle()
			.subscribeBy { openShareChooser(R.string.share_card, lastState.card.number) }

		RxView.clicks(buttonEdit)
			.applyThrottling()
			.bindToLifecycle()
			.subscribeBy { router.navigateTo(SCREEN_ADD_CARD, lastState.card.number) }
	}
}