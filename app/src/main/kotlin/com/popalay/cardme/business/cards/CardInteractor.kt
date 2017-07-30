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

    fun get(cardNumber: String): Flowable<Card> = cardRepository.get(cardNumber)
            .subscribeOn(Schedulers.io())

    fun setLastScanned(card: Card): Completable = cardRepository.setLastScanned(card)
            .subscribeOn(Schedulers.io())

    fun getLastScanned(): Single<Card> = cardRepository.getLastScanned()

    fun checkCardExist(creditCard: Card): Completable {
        val card = transform(creditCard)
        return cardRepository.cardIsNew(card)
                .flatMapCompletable { if (it) Completable.complete() else Completable.error(createCardExistError()) }
                .subscribeOn(Schedulers.io())
    }

    fun hasAllData(card: Card?, holderName: String): Single<Boolean> = Single.fromCallable {
        holderName.isNotBlank()
    }

    fun getCards(): Flowable<List<Card>> = cardRepository.getAll()
            .subscribeOn(Schedulers.io())

    fun getTrashedCards(): Flowable<List<Card>> = cardRepository.getAllTrashed()
            .subscribeOn(Schedulers.io())

    fun update(items: List<Card>): Completable {
        items.mapIndexed { index, card -> card.position = index }
        return cardRepository.update(items)
                .subscribeOn(Schedulers.io())
    }

    fun markAsTrash(card: Card): Completable = cardRepository.markAsTrash(card)
            .andThen(holderRepository.removeCard(card))
            .subscribeOn(Schedulers.io())

    fun restore(card: Card): Completable = holderRepository.addCard(card.holder.name, card)
            .andThen(cardRepository.restore(card))
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
