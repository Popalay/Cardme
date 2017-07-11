package com.popalay.cardme.presentation.screens.addcard

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.business.exception.ExceptionFactory
import com.popalay.cardme.business.holders.HolderInteractor
import com.popalay.cardme.business.settings.SettingsInteractor
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.setTo
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.observe
import com.stepango.rxdatabindings.setTo
import io.card.payment.CreditCard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AddCardViewModel @Inject constructor(
        private val router: CustomRouter,
        private val cardInteractor: CardInteractor,
        creditCard: CreditCard,
        holderInteractor: HolderInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel(), AddCardViewModelFacade {

    val holderName = ObservableString()
    val holderNames = ObservableArrayList<String>()
    val title = ObservableString()
    val canSave = ObservableBoolean()
    val showImage = ObservableBoolean()
    val card = ObservableField<Card>()

    val acceptClickListener: PublishRelay<Boolean> = PublishRelay.create<Boolean>()
    val editorActionListener: PublishRelay<Int> = PublishRelay.create<Int>()
    val errorDialogState: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()

    init {
        cardInteractor.transformCard(creditCard)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(card)
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        holderInteractor.getHolderNames()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderNames)
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        holderName.observe()
                .doOnNext { card.get()?.holder?.name = it.trim() }
                .doOnNext { canSave.set(!it.isNullOrBlank()) }
                .subscribeBy(onError = this::handleLocalError)
                .addTo(disposables)

        title.observe()
                .doOnNext { card.get()?.title = it.trim() }
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        acceptClickListener.applyThrottling()
                .map { card.get() }
                .flatMapCompletable(cardInteractor::save)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = this::handleLocalError, onComplete = router::exit)
                .addTo(disposables)

        editorActionListener.applyThrottling()
                .filter { it == EditorInfo.IME_ACTION_DONE }
                .doOnNext { acceptClickListener.accept(true) }
                .subscribeBy(this::handleLocalError)
                .addTo(disposables)

        errorDialogState
                .filter { !it }
                .subscribeBy {
                    router.exit()
                }.addTo(disposables)
    }

    override fun doOnShowCardExistsDialog(body: () -> Unit): Disposable {
        return errorDialogState.filter { it }.subscribe { body() }
    }

    override fun onShowCardExistsDialogDismiss() {
        errorDialogState.accept(false)
        router.exit()
    }

    private fun handleLocalError(throwable: Throwable) {
        handleBaseError(throwable)
        if (throwable !is AppException) return
        when (throwable.errorType) {
            ExceptionFactory.ErrorType.CARD_EXIST -> errorDialogState.accept(true)
            else -> {
            }
        }
    }

}
