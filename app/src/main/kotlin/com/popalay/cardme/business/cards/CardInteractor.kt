package com.popalay.cardme.business.cards

import com.popalay.cardme.R
import com.popalay.cardme.business.exception.ExceptionFactory
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.repositories.card.CardRepository
import com.popalay.cardme.data.repositories.debt.DebtRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardInteractor @Inject constructor(
        private val cardRepository: CardRepository,
        private val holderRepository: HolderRepository,
        private val debtRepository: DebtRepository
) {

    fun validateCard(creditCard: Card): Single<Card> {
        val card = transform(creditCard)
        return cardRepository.cardIsNew(card)
                .flatMap { if (it) Single.just(card) else Single.error<Card>(createCardExistError()) }
                .subscribeOn(Schedulers.io())
    }

    fun save(card: Card): Completable = cardRepository.save(card)
            .flatMapCompletable { holderRepository.updateCounts(it.holder) }
            .subscribeOn(Schedulers.io())

    fun hasAllData(card: Card?): Single<Boolean> = Single.fromCallable {
        card?.holder?.name?.isNotBlank() ?: false
    }

    fun getCards(): Flowable<List<Card>> = cardRepository.getAll().subscribeOn(Schedulers.io())

    fun getTrashedCards(): Flowable<List<Card>> = cardRepository.getAllTrashed().subscribeOn(Schedulers.io())

    fun getCardsByHolder(holderName: String): Flowable<List<Card>> = cardRepository.getAllByHolder(holderName)
            .subscribeOn(Schedulers.io())

    fun update(items: List<Card>): Completable {
        items.mapIndexed { index, card -> card.position = index }
        return cardRepository.update(items)
                .subscribeOn(Schedulers.io())
    }

    fun markAsTrash(card: Card): Completable = cardRepository.markAsTrash(card)
            .andThen(holderRepository.updateCounts(card.holder))
            .subscribeOn(Schedulers.io())

    fun restore(card: Card): Completable = cardRepository.restore(card)
            .andThen(holderRepository.updateCounts(card.holder))
            .subscribeOn(Schedulers.io())

    fun emptyTrash(): Completable = Singles.zip(
            cardRepository.removeTrashed().toSingleDefault(true),
            holderRepository.removeTrashed().toSingleDefault(true),
            debtRepository.removeTrashed().toSingleDefault(true))
            .toCompletable()
            .subscribeOn(Schedulers.io())

    private fun transform(card: Card): Card = card.apply {
        generatedBackgroundSeed = System.nanoTime()
    }

    private fun createCardExistError(): Throwable =
            ExceptionFactory.createError(ExceptionFactory.ErrorType.CARD_EXIST,
                    R.string.error_card_exist)
}
