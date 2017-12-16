package com.popalay.cardme.screens.addcard

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.interactor.CardInteractor
import com.popalay.cardme.domain.interactor.HolderInteractor
import com.popalay.cardme.domain.interactor.SettingsInteractor
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.clean
import com.popalay.cardme.utils.extensions.setTo
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.observe
import com.stepango.rxdatabindings.setTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class AddCardViewModel @Inject constructor(
        @Named(AddCardActivity.KEY_CARD_NUMBER) cardNumber: String,
        private val router: CustomRouter,
        cardInteractor: CardInteractor,
        holderInteractor: HolderInteractor,
        settingsInteractor: SettingsInteractor
) : BaseViewModel(), AddCardViewModelFacade {

    val holderName = ObservableString()
    val holderNames: ObservableArrayList<String> = ObservableArrayList()
    val title = ObservableString()
    val showImage = ObservableBoolean()
    val card = ObservableField<Card>()

    val editorActionListener: PublishRelay<Int> = PublishRelay.create<Int>()

    private val canSaveState: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()
    private val acceptClickListener: PublishRelay<Boolean> = PublishRelay.create<Boolean>()

    init {
        cardInteractor.get(cardNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(card)
                .doOnNext {
                    title.set(it.title)
                    holderName.set(it.holderName)
                }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        settingsInteractor.listenShowCardsBackground()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(showImage)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        holderInteractor.getNames()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderNames)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        Observables.combineLatest(
                holderName.observe().doOnNext { card.set(card.get().copy(holderName = it.clean())) },
                title.observe().doOnNext { card.set(card.get().copy(title = it.clean())) })
                .switchMapSingle { cardInteractor.hasAllData(card.get(), it.first) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(canSaveState::accept)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        acceptClickListener
                .applyThrottling()
                .filter { it && canSaveState.value }
                .map { card.get() }
                .switchMapSingle { cardInteractor.save(it).toSingleDefault(true) }
                .doOnNext { router.exit() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        editorActionListener
                .applyThrottling()
                .filter { it == EditorInfo.IME_ACTION_DONE }
                .doOnNext { acceptClickListener.accept(true) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

    override fun onAcceptClick() = acceptClickListener.accept(true)

    override fun onCanSaveStateChanged(): Observable<Boolean> = canSaveState

}

interface AddCardViewModelFacade {
    fun onAcceptClick()
    fun onCanSaveStateChanged(): Observable<Boolean>
}