/*
 * Created by popalay on 03.01.18 23:19
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 22:52
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.cast
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShareCardByNfcUseCase @Inject constructor(
	private val cardRepository: CardRepository
) : UseCase<ShareCardByNfcAction> {

	override fun apply(upstream: Observable<ShareCardByNfcAction>): ObservableSource<Result> =
		upstream.switchMap {
			cardRepository.save(it.card)
				.toSingleDefault(ShareCardByNfcResult.Success)
				.toObservable()
				.cast<ShareCardByNfcResult>()
				.onErrorReturn(ShareCardByNfcResult::Failure)
				.subscribeOn(Schedulers.io())
		}
}

data class ShareCardByNfcAction(val card: Card) : Action

sealed class ShareCardByNfcResult : Result {
	object Success : ShareCardByNfcResult()
	data class Failure(val throwable: Throwable) : ShareCardByNfcResult()
}