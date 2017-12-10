package com.popalay.cardme.domain.interactor

import com.popalay.cardme.domain.ExceptionFactory
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardInteractor @Inject constructor(
        private val cardRepository: CardRepository,
        private val holderInteractor: HolderInteractor
) {

    fun save(card: Card): Completable {
        return holderInteractor.save(Holder(name = card.holderName))
                .andThen(cardRepository.save(card.apply { isPending = false }))
                .subscribeOn(Schedulers.io())
    }

    fun savePending(card: Card): Completable {
        return cardRepository.contains(card.number)
                .flatMapCompletable { if (!it) Completable.complete() else Completable.error(createCardExistError()) }
                .andThen(holderInteractor.savePending(Holder(name = card.holderName)))
                .andThen(cardRepository.save(card.apply {
                    isPending = true
                    generatedBackgroundSeed = System.nanoTime()
                }))
                .subscribeOn(Schedulers.io())
    }

    fun get(cardNumber: String): Flowable<Card> = cardRepository.get(cardNumber)
            .subscribeOn(Schedulers.io())

    fun hasAllData(card: Card?, holderName: String): Single<Boolean> = Single.fromCallable {
        holderName.isNotBlank()
    }

    fun getAllNotTrashed(): Flowable<List<Card>> = cardRepository.getAllNotTrashed()
            .subscribeOn(Schedulers.io())

    fun getAllNotTrashedByHolder(holderName: String): Flowable<List<Card>>
            = cardRepository.getNotTrashedByHolder(holderName)
            .subscribeOn(Schedulers.io())

    fun getAllTrashed(): Flowable<List<Card>> = cardRepository.getAllTrashed()
            .subscribeOn(Schedulers.io())

    fun update(items: List<Card>): Completable {
        items.mapIndexed { index, card -> card.position = index }
        return cardRepository.update(items)
                .subscribeOn(Schedulers.io())
    }

    fun markAsTrash(card: Card): Completable = cardRepository.markAsTrash(card)
            .subscribeOn(Schedulers.io())

    fun restore(card: Card): Completable = cardRepository.restore(card)
            .subscribeOn(Schedulers.io())

    fun prepareForSharing(card: Card): Single<String> = cardRepository.toJson(card)
            .subscribeOn(Schedulers.io())

    fun getFromJson(source: String): Single<Card> = cardRepository.fromJson(source)
            .subscribeOn(Schedulers.io())

    private fun createCardExistError(): Throwable =
            ExceptionFactory.createError(ExceptionFactory.ErrorType.CARD_EXIST)
}
