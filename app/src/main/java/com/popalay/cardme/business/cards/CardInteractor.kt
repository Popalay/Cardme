package com.popalay.cardme.business.cards

import com.popalay.cardme.R
import com.popalay.cardme.business.exception.ExceptionFactory
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.repositories.card.CardRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardInteractor @Inject constructor(
        private val cardRepository: CardRepository,
        private val holderRepository: HolderRepository
) {

    fun validateCard(creditCard: Card): Single<Card> {
        val card = transform(creditCard)
        return cardRepository.cardIsNew(card)
                .flatMap { if (it) Single.just(card) else Single.error<Card>(createCardExistError()) }
                .subscribeOn(Schedulers.io())
    }

    fun save(card: Card): Completable = cardRepository.save(card)
            .andThen(holderRepository.updateCounts(card.holder))
            .subscribeOn(Schedulers.io())

    fun getCards(): Flowable<List<Card>> = cardRepository.getAll().subscribeOn(Schedulers.io())

    fun getCardsByHolder(holderId: String): Flowable<List<Card>> = cardRepository.getAllByHolder(holderId)
            .subscribeOn(Schedulers.io())

    fun updateCards(items: List<Card>): Completable {
        items.mapIndexed { index, card -> card.position = index }
        return cardRepository.save(items)
                .subscribeOn(Schedulers.io())
    }

    fun removeCard(card: Card): Completable = cardRepository.remove(card)
            .andThen(holderRepository.updateCounts(card.holder))
            .subscribeOn(Schedulers.io())

    private fun transform(card: Card): Card = card.apply {
        generatedBackgroundSeed = System.nanoTime()
    }

    private fun createCardExistError(): Throwable =
            ExceptionFactory.createError(ExceptionFactory.ErrorType.CARD_EXIST,
                    R.string.error_card_exist)
}
