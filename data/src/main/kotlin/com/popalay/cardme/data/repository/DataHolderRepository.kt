package com.popalay.cardme.data.repository

import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataHolderRepository @Inject constructor(
        private val cardDao: CardDao
) : HolderRepository {

    override fun addCard(holderName: String, card: Card): Completable = Completable.complete() /*{
        //TODO create holder if needed
        return Completable.fromAction {
            cardDao.insertOrUpdate(card.apply { this.holderName = holderName })
        }
    }*/

    /*fun addCard(holderName: String, card: DataCard): Completable = RxRealm.doTransactional {
        it.where(DataCard::class.java).equalTo(DataCard.NUMBER, card.number).findAll().deleteAllFromRealm()
    }.andThen(RxRealm.doTransactional {
        val holder = it.where(DataHolder::class.java).equalTo(DataHolder.NAME, holderName)
                .findFirst() ?: it.createObject(DataHolder::class.java, holderName)
        card.holder = holder
        val realmCard = it.copyToRealmOrUpdate(card)
        if (!holder.cards.contains(realmCard)) holder.cards.add(realmCard)

        updateTrashFlag(it)
    })*/

    override fun addDebt(holderName: String, debt: Debt): Completable = Completable.complete()/*RxRealm.doTransactional {
        val holder = it.where(DataHolder::class.java).equalTo(DataHolder.NAME, holderName)
                .findFirst() ?: it.createObject(DataHolder::class.java, holderName)
        debt.holder = holder
        val realmDebt = it.copyToRealmOrUpdate(debt)
        if (!holder.debts.contains(realmDebt)) holder.debts.add(realmDebt)

        updateTrashFlag(it)
    }*/

    override fun removeCard(card: Card): Completable = Completable.complete()/*RxRealm.doTransactional {
        val holder = it.where(DataHolder::class.java).equalTo(DataHolder.NAME, card.holderName).findFirst()
        //val realmCard = it.where(DataCard::class.java).equalTo(DataCard.NUMBER, card.number).findFirst()
        //holder.cards.remove(realmCard)

        updateTrashFlag(it)
    }*/

    override fun removeDebt(debt: Debt): Completable = Completable.complete()/*RxRealm.doTransactional {
        val holder = it.where(DataHolder::class.java).equalTo(DataHolder.NAME, debt.holder.name).findFirst()
        val realmDebt = it.where(DataDebt::class.java).equalTo(DataDebt.ID, debt.id).findFirst()
        holder.debts.remove(realmDebt)

        updateTrashFlag(it)
    }*/

    override fun getAll(): Flowable<List<Holder>> = Flowable.empty()/*RxRealm.listenList {
        it.where(DataHolder::class.java)
                .equalTo(DataHolder.IS_TRASH, false)
                .findAllSorted(DataHolder.NAME)
    }
*/
    override fun get(holderName: String): Flowable<Holder> = Flowable.empty()/*RxRealm.listenElement {
        it.where(DataHolder::class.java).equalTo(DataHolder.NAME, holderName).findAll()
    }*/

    override fun removeTrashed(): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Holder::class.java).equalTo(DataHolder.IS_TRASH, true).findAll().deleteAllFromRealm()
    }*/

    private fun updateTrashFlag() {
/*        val intoTrash = realm.where(DataHolder::class.java)
                //TODO.isEmpty(DataHolder.CARDS)
                .isEmpty(DataHolder.DEBTS)
                .findAll()

        for (item in intoTrash) {
            item.isTrash = true
        }

        val fromTrash = realm.where(DataHolder::class.java)
                //TODO.isNotEmpty(DataHolder.CARDS)
                .or()
                .isNotEmpty(DataHolder.DEBTS)
                .findAll()

        for (item in fromTrash) {
            item.isTrash = false
        }*/
    }
}