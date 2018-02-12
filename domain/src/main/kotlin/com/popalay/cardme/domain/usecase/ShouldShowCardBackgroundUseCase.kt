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
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShouldShowCardBackgroundUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCase<ShouldShowCardBackgroundUseCase.Action, ShouldShowCardBackgroundUseCase.Result> {

    override fun apply(upstream: Observable<ShouldShowCardBackgroundUseCase.Action>): ObservableSource<Result> =
        upstream.switchMap {
            settingsRepository.listen()
                .map { it.isCardBackground }
                .distinctUntilChanged()
                .toObservable()
                .map(Result::Success)
                .cast(Result::class.java)
                .onErrorReturn(Result::Failure)
                .subscribeOn(Schedulers.io())
        }

    object Action : UseCase.Action

    sealed class Result : UseCase.Result {
        data class Success(val show: Boolean) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }
}
