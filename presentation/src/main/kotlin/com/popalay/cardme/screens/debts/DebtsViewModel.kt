package com.popalay.cardme.screens.debts

import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.interactor.DebtsInteractor
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.screens.SCREEN_ADD_DEBT
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.setTo
import com.popalay.cardme.utils.recycler.DiffObservableList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class DebtsViewModel @Inject constructor(
        router: CustomRouter,
        debtsInteractor: DebtsInteractor
) : BaseViewModel() {

    var debts = DiffObservableList<Debt>()

    val addDebtClickPublisher: PublishRelay<Boolean> = PublishRelay.create<Boolean>()

    val onSwiped: PublishRelay<Int> = PublishRelay.create()
    val onUndoSwipe: PublishRelay<Boolean> = PublishRelay.create()

    init {
        debtsInteractor.getAllNotTrashed()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(debts)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        onSwiped
                .map(debts::get)
                .flatMapSingle { debtsInteractor.markAsTrash(it).toSingle { it } }
                .switchMap { debt -> onUndoSwipe.applyThrottling().filter { it }.map { debt } }
                .flatMapCompletable(debtsInteractor::restore)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        addDebtClickPublisher
                .applyThrottling()
                .doOnNext { router.navigateTo(SCREEN_ADD_DEBT) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }
}
