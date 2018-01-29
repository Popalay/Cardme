package com.popalay.cardme.screens.carddetails

import com.popalay.cardme.base.mvi.IntentFilter
import com.popalay.cardme.base.mvi.MviViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.usecase.Action
import com.popalay.cardme.domain.usecase.ActionTransformer
import com.popalay.cardme.domain.usecase.CardDetailsResult
import com.popalay.cardme.domain.usecase.CardDetailsUseCase
import com.popalay.cardme.domain.usecase.CheckNfcAction
import com.popalay.cardme.domain.usecase.CheckNfcResult
import com.popalay.cardme.domain.usecase.CheckNfcUseCase
import com.popalay.cardme.domain.usecase.GetCardDetailsAction
import com.popalay.cardme.domain.usecase.MoveCardToTrashAction
import com.popalay.cardme.domain.usecase.MoveCardToTrashResult
import com.popalay.cardme.domain.usecase.MoveCardToTrashUseCase
import com.popalay.cardme.domain.usecase.Result
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundAction
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundResult
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundUseCase
import com.popalay.cardme.utils.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class CardDetailsViewModel @Inject constructor(
	private val router: CustomRouter,
	private val cardDetailsUseCase: CardDetailsUseCase,
	private val shouldShowCardBackgroundUseCase: ShouldShowCardBackgroundUseCase,
	private val checkNfcUseCase: CheckNfcUseCase,
	private val moveCardToTrashUseCase: MoveCardToTrashUseCase
) : MviViewModel<CardDetailsViewState, CardDetailsIntent>() {

	override val intentFilter
		get() = IntentFilter<CardDetailsIntent> {
			it.publish {
				Observable.merge<CardDetailsIntent>(
					listOf(
						it.ofType<CardDetailsIntent.Initial.GetCard>().take(1),
						it.ofType<CardDetailsIntent.Initial.GetShouldShowBackground>().take(1),
						it.ofType<CardDetailsIntent.Initial.CheckNfc>().take(1),
						it.notOfType(CardDetailsIntent.Initial::class.java)
					)
				)
			}
		}

	override val actions
		get() = ActionTransformer {
			it.publish {
				Observable.merge(
					listOf(
						it.ofType<GetCardDetailsAction>().compose(cardDetailsUseCase),
						it.ofType<ShouldShowCardBackgroundAction>().compose(shouldShowCardBackgroundUseCase),
						it.ofType<CheckNfcAction>().compose(checkNfcUseCase),
						it.ofType<MoveCardToTrashAction>().compose(moveCardToTrashUseCase),
						it.ofType<AnimateButtonsAction>().map { AnimateButtonsResult }
					)
				)
			}
		}

	override fun actionFromIntent(intent: CardDetailsIntent): Action = when (intent) {
		is CardDetailsIntent.Initial.GetCard -> GetCardDetailsAction(intent.number)
		is CardDetailsIntent.MarkAsTrash -> MoveCardToTrashAction(intent.card)
		is CardDetailsIntent.ShareByNfc -> TODO()
		CardDetailsIntent.Initial.GetShouldShowBackground -> ShouldShowCardBackgroundAction
		CardDetailsIntent.Initial.CheckNfc -> CheckNfcAction
		CardDetailsIntent.EnterTransitionFinished -> AnimateButtonsAction
	}

	override fun compose(): Observable<CardDetailsViewState> = intentsSubject
		.compose(intentFilter)
		.map(::actionFromIntent)
		.compose(actions)
		.scan(CardDetailsViewState.idle(), ::reduce)
		.replay(1)
		.autoConnect(0)
		.observeOn(AndroidSchedulers.mainThread())

	override fun reduce(oldState: CardDetailsViewState, result: Result): CardDetailsViewState = with(result) {
		when (this) {
			is CardDetailsResult -> when (this) {
				is CardDetailsResult.Success -> oldState.copy(card = card)
				is CardDetailsResult.Failure -> oldState.copy(error = throwable)
			}
			is ShouldShowCardBackgroundResult -> when (this) {
				is ShouldShowCardBackgroundResult.Success -> oldState.copy(showBackground = show)
				is ShouldShowCardBackgroundResult.Failure -> oldState.copy(error = throwable)
			}
			is CheckNfcResult -> when (this) {
				is CheckNfcResult.Success -> oldState.copy(nfcEnabled = supportsNfc)
				is CheckNfcResult.Failure -> oldState.copy(error = throwable, nfcEnabled = false)
			}
			is MoveCardToTrashResult -> when (this) {
				MoveCardToTrashResult.Success -> {
					router.exit()
					oldState
				}
				is MoveCardToTrashResult.Failure -> oldState.copy(error = throwable)
			}
			AnimateButtonsResult -> oldState.copy(animateButtons = true)
			else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
		}
	}

	private object AnimateButtonsAction : Action
	private object AnimateButtonsResult : Result

/*    fun onShareCard(): Observable<String> = shareCardClick
            .applyThrottling()
            .map { card.get().number }

    fun onShareCardUsingNfc(): Observable<String> = shareNfcCardClick
            .applyThrottling()
            .filter { enableShareNfc.get() }
            .flatMapSingle { cardInteractor.prepareForSharing(card.get()) }

    fun getCardShareNfcObject() = cardInteractor.prepareForSharing(card.get())*/
}