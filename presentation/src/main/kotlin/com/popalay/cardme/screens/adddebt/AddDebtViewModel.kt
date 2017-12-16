package com.popalay.cardme.screens.adddebt

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.jakewharton.rxrelay2.PublishRelay
import com.popalay.cardme.base.BaseViewModel
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.domain.interactor.DebtsInteractor
import com.popalay.cardme.domain.interactor.HolderInteractor
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.utils.extensions.applyThrottling
import com.popalay.cardme.utils.extensions.clean
import com.popalay.cardme.utils.extensions.setTo
import com.stepango.rxdatabindings.ObservableString
import com.stepango.rxdatabindings.observe
import com.stepango.rxdatabindings.setTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AddDebtViewModel @Inject constructor(
        router: CustomRouter,
        holderInteractor: HolderInteractor,
        debtsInteractor: DebtsInteractor
) : BaseViewModel() {

    val to = ObservableString()
    val message = ObservableString()
    val canSave = ObservableBoolean()
    val holderNames = ObservableArrayList<String>()

    val addClick: PublishRelay<Boolean> = PublishRelay.create<Boolean>()

    private val debt = ObservableField<Debt>(Debt())

    init {

        holderInteractor.getNames()
                .observeOn(AndroidSchedulers.mainThread())
                .setTo(holderNames)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        Observables.combineLatest(to.observe().doOnNext { debt.set(debt.get().copy(holderName = it.clean())) },
                message.observe().doOnNext { debt.set(debt.get().copy(message = it.clean())) })
                .map { !it.first.isNullOrBlank() && !it.second.isNullOrBlank() }
                .setTo(canSave)
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)

        addClick.applyThrottling()
                .filter { it && canSave.get() }
                .switchMapSingle { debtsInteractor.save(debt.get()).toSingleDefault(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { router.exit() }
                .subscribeBy(this::handleBaseError)
                .addTo(disposables)
    }
}
