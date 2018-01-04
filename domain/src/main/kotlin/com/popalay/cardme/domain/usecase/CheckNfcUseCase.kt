/*
 * Created by popalay on 04.01.18 23:15
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 1:16
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.repository.DeviceRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckNfcUseCase @Inject constructor(
        private val deviceRepository: DeviceRepository
) : UseCase<CheckNfcAction> {

    override fun apply(upstream: Observable<CheckNfcAction>): ObservableSource<Result> =
            upstream.switchMap {
                Single.fromCallable(deviceRepository::supportNfc)
                        .toObservable()
                        .map(ShouldShowCardBackgroundResult::Success)
                        .cast(ShouldShowCardBackgroundResult::class.java)
                        .onErrorReturn(ShouldShowCardBackgroundResult::Failure)
                        .subscribeOn(Schedulers.io())
            }
}

object CheckNfcAction : Action

sealed class CheckNfcResult : Result {
    data class Success(val supportsNfc: Boolean) : CheckNfcResult()
    data class Failure(val throwable: Throwable) : CheckNfcResult()
}
