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
import javax.inject.Inject

class HolderNamesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val holderRepository: HolderRepository
) : UseCase<HolderNamesUseCase.Action, HolderNamesUseCase.Result> {

    override fun apply(upstream: Observable<Action>): ObservableSource<Result> = upstream
        .distinctUntilChanged()
        .switchMapSingle { action ->
            holderRepository.getAll()
                .first(listOf())
                .flattenAsObservable { it }
                .map { it.name }
                .mergeWith(deviceRepository.getContactsNames().flattenAsObservable { it })
                .distinct()
                .filter { it.startsWith(action.query) }
                .filter { it.isNotBlank() }
                .toSortedList()
                .map(Result::Success)
        }

    data class Action(val query: String) : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val names: List<String>) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}