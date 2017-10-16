package com.popalay.cardme.data.repository

import com.popalay.cardme.data.dao.HolderDao
import com.popalay.cardme.data.toData
import com.popalay.cardme.data.toDomain
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataHolderRepository @Inject constructor(
        private val holderDao: HolderDao
) : HolderRepository {

    override fun save(holder: Holder): Completable = Completable.fromAction {
        holderDao.insertOrUpdate(holder.toData())
    }

    override fun getAll(): Flowable<List<Holder>> = holderDao.getAllNotTrashed()
            .map { it.map { it.toDomain() } }

    override fun get(holderName: String): Flowable<Holder> = holderDao.get(holderName)
            .map { it.toDomain() }

    override fun removeTrashed(): Completable = Completable.complete()/*RxRealm.doTransactional {
        it.where(Holder::class.java).equalTo(Holder.IS_TRASH, true).findAll().deleteAllFromRealm()
    }*/

    private fun updateTrashFlag() {
/*        val intoTrash = realm.where(Holder::class.java)
                //TODO.isEmpty(Holder.CARDS)
                .isEmpty(Holder.DEBTS)
                .findAll()

        for (item in intoTrash) {
            item.isTrash = true
        }

        val fromTrash = realm.where(Holder::class.java)
                //TODO.isNotEmpty(Holder.CARDS)
                .or()
                .isNotEmpty(Holder.DEBTS)
                .findAll()

        for (item in fromTrash) {
            item.isTrash = false
        }*/
    }
}