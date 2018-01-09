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
import com.popalay.cardme.domain.usecase.EditCardAction
import com.popalay.cardme.domain.usecase.EditCardResult
import com.popalay.cardme.domain.usecase.EditCardUseCase
import com.popalay.cardme.domain.usecase.GetCardDetailsAction
import com.popalay.cardme.domain.usecase.GetHolderNamesAction
import com.popalay.cardme.domain.usecase.HolderNamesResult
import com.popalay.cardme.domain.usecase.HolderNamesUseCase
import com.popalay.cardme.domain.usecase.MoveCardToTrashAction
import com.popalay.cardme.domain.usecase.MoveCardToTrashResult
import com.popalay.cardme.domain.usecase.MoveCardToTrashUseCase
import com.popalay.cardme.domain.usecase.Result
import com.popalay.cardme.domain.usecase.SaveCardAction
import com.popalay.cardme.domain.usecase.SaveCardResult
import com.popalay.cardme.domain.usecase.SaveCardUseCase
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundAction
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundResult
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundUseCase
import com.popalay.cardme.domain.usecase.ValidateCardAction
import com.popalay.cardme.domain.usecase.ValidateCardResult
import com.popalay.cardme.domain.usecase.ValidateCardUseCase
import com.popalay.cardme.utils.extensions.notOfType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject


class CardDetailsViewModel @Inject constructor(
    private val router: CustomRouter,
    private val cardDetailsUseCase: CardDetailsUseCase,
    private val validateCardUseCase: ValidateCardUseCase,
    private val saveCardUseCase: SaveCardUseCase,
    private val shouldShowCardBackgroundUseCase: ShouldShowCardBackgroundUseCase,
    private val holderNamesUseCase: HolderNamesUseCase,
    private val checkNfcUseCase: CheckNfcUseCase,
    private val moveCardToTrashUseCase: MoveCardToTrashUseCase,
    private val editCardUseCase: EditCardUseCase
) : MviViewModel<CardDetailsViewState, CardDetailsIntent>() {

    companion object {

        private const val BUTTONS_ANIMATION_DURATION = 200L
    }

    override val intentFilter
        get() = IntentFilter<CardDetailsIntent> {
            it.publish {
                Observable.merge<CardDetailsIntent>(
                    listOf(
                        it.ofType<CardDetailsIntent.Initial.GetCard>().take(1),
                        it.ofType<CardDetailsIntent.Initial.GetHolderNames>().take(1),
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
                        it.ofType<GetCardDetailsAction>().compose(cardDetailsUseCase)
                            .concatWith {
                                Observable.timer(BUTTONS_ANIMATION_DURATION, MILLISECONDS, Schedulers.io())
                                    .map { ButtonsAnimationFinishedResult }
                            }
                            .cast(Result::class.java)
                            .startWith(ButtonsAnimationStartedResult),
                        it.ofType<ShouldShowCardBackgroundAction>().compose(shouldShowCardBackgroundUseCase),
                        it.ofType<GetHolderNamesAction>().compose(holderNamesUseCase),
                        it.ofType<ValidateCardAction>().compose(validateCardUseCase),
                        it.ofType<SaveCardAction>().compose(saveCardUseCase),
                        it.ofType<CheckNfcAction>().compose(checkNfcUseCase),
                        it.ofType<EditCardAction>().compose(editCardUseCase),
                        it.ofType<MoveCardToTrashAction>().compose(moveCardToTrashUseCase)
                    )
                )
            }
        }

    override fun actionFromIntent(intent: CardDetailsIntent): Action = when (intent) {
        is CardDetailsIntent.Initial.GetCard -> GetCardDetailsAction(intent.number)
        is CardDetailsIntent.Initial.GetHolderNames -> GetHolderNamesAction
        is CardDetailsIntent.Initial.GetShouldShowBackground -> ShouldShowCardBackgroundAction
        is CardDetailsIntent.Initial.CheckNfc -> CheckNfcAction
        is CardDetailsIntent.CardNameChanged -> ValidateCardAction(intent.card)
        is CardDetailsIntent.CardTitleChanged -> ValidateCardAction(intent.card)
        is CardDetailsIntent.MarkAsTrash -> MoveCardToTrashAction(intent.card)
        is CardDetailsIntent.ShareCard -> TODO()
        is CardDetailsIntent.Edit -> when (intent.inEditMode) {
            true -> SaveCardAction(intent.card)
            false -> EditCardAction(intent.card)
        }
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
                is CardDetailsResult.Idle -> oldState
                is CardDetailsResult.Success -> oldState.copy(card = card, inEditMode = card.isPending)
                is CardDetailsResult.Failure -> oldState.copy(error = throwable)
            }
            is HolderNamesResult -> when (this) {
                is HolderNamesResult.Success -> oldState.copy(holderNames = names)
                is HolderNamesResult.Failure -> oldState.copy(error = throwable)
            }
            is ShouldShowCardBackgroundResult -> when (this) {
                is ShouldShowCardBackgroundResult.Success -> oldState.copy(showBackground = show)
                is ShouldShowCardBackgroundResult.Failure -> oldState.copy(error = throwable)
            }
            is ValidateCardResult -> when (this) {
                is ValidateCardResult.Success -> oldState.copy(canSave = valid)
                is ValidateCardResult.Failure -> oldState.copy(error = throwable, canSave = false)
                is ValidateCardResult.Idle -> oldState.copy(card = card)
            }
            is SaveCardResult -> when (this) {
                is SaveCardResult.Success -> oldState.copy(inEditMode = false)
                is SaveCardResult.Failure -> oldState.copy(error = throwable)
                is SaveCardResult.Idle -> oldState
            }
            is CheckNfcResult -> when (this) {
                is CheckNfcResult.Success -> oldState.copy(nfcEnabled = supportsNfc)
                is CheckNfcResult.Failure -> oldState.copy(error = throwable, nfcEnabled = false)
            }
            is MoveCardToTrashResult -> when (this) {
                is MoveCardToTrashResult.Success -> {
                    router.exit()
                    oldState
                }
                is MoveCardToTrashResult.Failure -> oldState.copy(error = throwable)
            }
            is EditCardResult -> when (this) {
                is EditCardResult.Idle -> oldState.copy(inEditMode = true, animateButtons = true)
                is EditCardResult.Success -> oldState.copy(inEditMode = true, animateButtons = false)
            }
            is ButtonsAnimationFinishedResult -> oldState.copy(animateButtons = false)
            is ButtonsAnimationStartedResult -> oldState.copy(animateButtons = true)
            else -> throw IllegalStateException("Can not reduce state for result ${javaClass.name}")
        }
    }

    private object ButtonsAnimationFinishedResult : Result
    private object ButtonsAnimationStartedResult : Result

/*    fun onShareCard(): Observable<String> = shareCardClick
            .applyThrottling()
            .map { card.get().number }

    fun onShareCardUsingNfc(): Observable<String> = shareNfcCardClick
            .applyThrottling()
            .filter { enableShareNfc.get() }
            .flatMapSingle { cardInteractor.prepareForSharing(card.get()) }

    fun getCardShareNfcObject() = cardInteractor.prepareForSharing(card.get())*/
}