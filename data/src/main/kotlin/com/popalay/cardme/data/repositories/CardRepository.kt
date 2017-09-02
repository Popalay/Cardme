package com.popalay.cardme.data.repositories

import com.google.gson.Gson
import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.models.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
        private val cardDao: CardDao,
        private val gson: Gson
) {

    fun get(cardNumber: String): Flowable<Card> = Flowable.empty()/*RxRealm.listenElement {
        it.where(Card::class.java).equalTo(Card.NUMBER, cardNumber).findAll()
    }*/

    fun update(cards: List<Card>): Completable = Completable.complete()
    /*RxRealm.doTransactional { it.copyToRealmOrUpdate(cards) }*/

    fun getAll(): Flowable<List<Card>> = cardDao.getAllNotTrashed()

    fun getAllTrashed(): Flowable<List<Card>> = Flowable.empty()/*RxRealm.listenList {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, true)
                .findAllSorted(Card.POSITION, Sort.ASCENDING, Card.HOLDER_NAME, Sort.ASCENDING)
    }*/

    fun markAsTrash(card: Card): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst().isTrash = true
    }*/

    fun removeTrashed(): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.IS_TRASH, true).findAll().deleteAllFromRealm()
    }*/

    fun restore(card: Card): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Card::class.java).equalTo(Card.NUMBER, card.number).findFirst().isTrash = false
    }
*/
    fun cardIsNew(cardNumber: String): Single<Boolean> = Single.just(true)/*RxRealm.getElement {
        it.where(Card::class.java)
                .equalTo(Card.IS_TRASH, false)
                .equalTo(Card.NUMBER, cardNumber).findFirst()
    }.isEmpty*/

    fun prepareForSharing(card: Card): Single<String> = Single.fromCallable { gson.toJson(card) }

    fun getFromJson(source: String): Single<Card> = Single.fromCallable {
        gson.fromJson<Card>(source, Card::class.java)
    }

}
