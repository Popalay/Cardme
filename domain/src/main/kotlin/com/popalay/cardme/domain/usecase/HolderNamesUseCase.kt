/*
 * Created by popalay on 26.12.17 23:36
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 16.12.17 19:54
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.DeviceRepository
import com.popalay.cardme.domain.repository.HolderRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HolderNamesUseCase @Inject constructor(
        private val deviceRepository: DeviceRepository,
        private val holderRepository: HolderRepository
) : UseCase<GetHolderNamesAction> {

    override fun apply(upstream: Observable<GetHolderNamesAction>): ObservableSource<Result> =
            upstream.switchMap {
                Flowables.combineLatest(
                        holderRepository.getAll(),
                        deviceRepository.getContactsNames().toFlowable(),
                        this::transform)
                        .toObservable()
                        .map(HolderNamesResult::Success)
                        .cast(HolderNamesResult::class.java)
                        .onErrorReturn(HolderNamesResult::Failure)
                        .startWith(HolderNamesResult.Idle)
                        .subscribeOn(Schedulers.io())
            }

    private fun transform(holders: List<Holder>, contacts: List<String>) =
            holders.map(Holder::name).toMutableList().apply {
                        addAll(contacts)
                        filter(String::isNotBlank).distinct().sorted()
                    }
}

object GetHolderNamesAction : Action

sealed class HolderNamesResult : Result {
    data class Success(val names: List<String>) : HolderNamesResult()
    data class Failure(val throwable: Throwable) : HolderNamesResult()
    object Idle : HolderNamesResult()
}
