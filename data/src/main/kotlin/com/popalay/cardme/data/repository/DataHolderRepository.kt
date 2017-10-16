package com.popalay.cardme.data.repository

import com.popalay.cardme.data.dao.HolderDao
import com.popalay.cardme.data.toData
import com.popalay.cardme.data.toDomain
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataHolderRepository @Inject constructor(
        private val holderDao: HolderDao
) : HolderRepository {

    override fun save(data: Holder): Completable = Completable.fromAction {
        holderDao.insertOrUpdate(data.toData())
    }

    override fun markAsTrash(data: Holder): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun restore(data: Holder): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contains(id: String): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(): Flowable<List<Holder>> = holderDao.getAll()
            .map { it.map { it.toDomain() } }

    override fun getAllTrashed(): Flowable<List<Holder>> = holderDao.getAllTrashed()
            .map { it.map { it.toDomain() } }

    override fun getAllNotTrashed(): Flowable<List<Holder>> = holderDao.getAllNotTrashed()
            .map { it.map { it.toDomain() } }

    override fun get(id: String): Flowable<Holder> = holderDao.get(id)
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