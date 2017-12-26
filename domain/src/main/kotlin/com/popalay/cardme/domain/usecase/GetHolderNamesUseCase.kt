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
import com.popalay.cardme.domain.usecase.GetHolderNamesAction.HolderNamesResultError
import com.popalay.cardme.domain.usecase.GetHolderNamesAction.HolderNamesResultSuccess
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetHolderNamesUseCase @Inject constructor(
        private val deviceRepository: DeviceRepository,
        private val holderRepository: HolderRepository
) {

    fun execute(): Flowable<GetHolderNamesAction> = Flowables.combineLatest(
            holderRepository.getAll(),
            deviceRepository.getContactsNames().toFlowable(),
            this::transform)
            .map { HolderNamesResultSuccess(it) as GetHolderNamesAction }
            .onErrorReturn { HolderNamesResultError(it) }
            .subscribeOn(Schedulers.io())

    private fun transform(holders: List<Holder>, contacts: List<String>): List<String> {
        val names = holders.map { it.name }.toMutableList()
        names.addAll(contacts)
        return names.filter(String::isNotBlank).distinct().sorted()
    }
}

sealed class GetHolderNamesAction : Action {
    data class HolderNamesResultSuccess(val names: List<String>) : GetHolderNamesAction()
    data class HolderNamesResultError(val throwable: Throwable) : GetHolderNamesAction()
}