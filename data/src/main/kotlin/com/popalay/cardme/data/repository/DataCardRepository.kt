package com.popalay.cardme.data.repository

import com.google.gson.Gson
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataCardRepository @Inject constructor(
        private val cardDao: CardDao,
        private val gson: Gson
) : CardRepository {

    override fun get(cardNumber: String): Flowable<Card> = Flowable.empty()/*RxRealm.listenElement {
        it.where(DataCard::class.java).equalTo(DataCard.NUMBER, cardNumber).findAll()
    }*/

    override fun update(cards: List<Card>): Completable = Completable.complete()
    /*RxRealm.doTransactional { it.copyToRealmOrUpdate(cards) }*/

    override fun getAll(): Flowable<List<Card>> = cardDao.getAllNotTrashed()

    override fun getAllTrashed(): Flowable<List<Card>> = Flowable.empty()/*RxRealm.listenList {
        it.where(DataCard::class.java)
                .equalTo(DataCard.IS_TRASH, true)
                .findAllSorted(DataCard.POSITION, Sort.ASCENDING, DataCard.HOLDER_NAME, Sort.ASCENDING)
    }*/

    override fun markAsTrash(card: Card): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(DataCard::class.java).equalTo(DataCard.NUMBER, card.number).findFirst().isTrash = true
    }*/

    override fun removeTrashed(): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(DataCard::class.java).equalTo(DataCard.IS_TRASH, true).findAll().deleteAllFromRealm()
    }*/

    override fun restore(card: Card): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(DataCard::class.java).equalTo(DataCard.NUMBER, card.number).findFirst().isTrash = false
    }*/

    override fun cardIsNew(cardNumber: String): Single<Boolean> = Single.just(true)/*RxRealm.getElement {
        it.where(DataCard::class.java)
                .equalTo(DataCard.IS_TRASH, false)
                .equalTo(DataCard.NUMBER, cardNumber).findFirst()
    }.isEmpty*/

    override fun prepareForSharing(card: Card): Single<String> = Single.fromCallable { gson.toJson(card) }

    override fun getFromJson(source: String): Single<Card> = Single.fromCallable {
        gson.fromJson<Card>(source, Card::class.java)
    }

}
