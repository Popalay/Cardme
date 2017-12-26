/*
 * Created by popalay on 26.12.17 23:24
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 10.09.17 1:25
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.repository.SettingsRepository
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundAction.ShowCardBackgroundError
import com.popalay.cardme.domain.usecase.ShouldShowCardBackgroundAction.ShowCardBackgroundSuccess
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShouldShowCardBackgroundUseCase @Inject constructor(
        private val settingsRepository: SettingsRepository
) {

    fun execute(): Flowable<ShouldShowCardBackgroundAction> = settingsRepository.listen()
            .distinctUntilChanged()
            .map { it.isCardBackground }
            .distinctUntilChanged()
            .map { ShowCardBackgroundSuccess(it) as ShouldShowCardBackgroundAction }
            .onErrorReturn { ShowCardBackgroundError(it) }
            .subscribeOn(Schedulers.io())
}

sealed class ShouldShowCardBackgroundAction : Action {
    data class ShowCardBackgroundSuccess(val show: Boolean) : ShouldShowCardBackgroundAction()
    data class ShowCardBackgroundError(val throwable: Throwable) : ShouldShowCardBackgroundAction()
}