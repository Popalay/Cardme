package com.popalay.cardme.presentation.screens.debts

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.business.debts.DebtsInteractor
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.presentation.base.BaseViewModel
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.SCREEN_ADD_DEBT
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.setTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class DebtsViewModel @Inject constructor(
        router: CustomRouter,
        debtsInteractor: DebtsInteractor
) : BaseViewModel(), DebtsViewModelFacade {

    val debts: ObservableList<Debt> = ObservableArrayList()

    val addDebtClickPublisher: PublishRelay<Boolean> = PublishRelay.create<Boolean>()

    val onSwiped: PublishRelay<Int> = PublishRelay.create()
    val onUndoSwipe: PublishRelay<Boolean> = PublishRelay.create()

    val createAddDebtTransition: PublishRelay<Bundle> = PublishRelay.create()

    init {
        debtsInteractor.getDebts()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(debts)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        addDebtClickPublisher.applyThrottling()
                //TODO make interaction with activity
                //.flatMap { createAddDebtTransition }
                .doOnNext { router.navigateToWithTransition(SCREEN_ADD_DEBT, transition = Bundle()) }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        onSwiped
                .map(debts::get)
                .flatMapSingle { debt -> debtsInteractor.remove(debt).toSingle { debt } }
                .flatMap { debt -> onUndoSwipe.filter { it }.map { debt } }
                .flatMapCompletable(debtsInteractor::save)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }

    override fun createAddDebtTransition(): Observable<Bundle> = createAddDebtTransition
}
