/*
 * Created by popalay on 26.12.17 23:24
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 10.09.17 1:25
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.repository.SettingsRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.cast
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShouldShowCardBackgroundUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCase<ShouldShowCardBackgroundAction> {

    override fun apply(upstream: Observable<ShouldShowCardBackgroundAction>): ObservableSource<Result> =
        upstream.switchMap {
            settingsRepository.listen()
                .map { it.isCardBackground }
                .distinctUntilChanged()
                .toObservable()
                .map(ShouldShowCardBackgroundResult::Success)
                .cast<ShouldShowCardBackgroundResult>()
                .onErrorReturn(ShouldShowCardBackgroundResult::Failure)
                .subscribeOn(Schedulers.io())
        }
}

object ShouldShowCardBackgroundAction : Action

sealed class ShouldShowCardBackgroundResult : Result {
    data class Success(val show: Boolean) : ShouldShowCardBackgroundResult()
    data class Failure(val throwable: Throwable) : ShouldShowCardBackgroundResult()
}
