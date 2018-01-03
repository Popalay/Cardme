package com.popalay.cardme.data.repository

import com.popalay.cardme.data.dao.CardDao
import com.popalay.cardme.data.dao.DebtDao
import com.popalay.cardme.data.dao.HolderDao
import com.popalay.cardme.data.toData
import com.popalay.cardme.data.toDomain
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.HolderRepository
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.Maybes
import javax.inject.Inject
import com.popalay.cardme.data.model.Holder as DataHolder

@Reusable
class HolderRepository @Inject constructor(
        private val holderDao: HolderDao,
        private val cardDao: CardDao,
        private val debtDao: DebtDao
) : HolderRepository {

    override fun save(data: Holder): Completable = Completable.fromAction {
        holderDao.insertOrIgnore(data.toData())
    }

    override fun markAsTrash(data: Holder): Completable = Completable.fromAction {
        holderDao.insertOrUpdate(data.copy(isTrash = true).toData())
    }

    override fun restore(data: Holder): Completable = Completable.fromAction {
        holderDao.insertOrUpdate(data.copy(isTrash = true).toData())
    }

    override fun contains(id: String): Single<Boolean> = holderDao.getCount(id).map { it > 0 }

    override fun getAll(): Flowable<List<Holder>> = holderDao.getAll()
            .flatMap(::addCounts)

    override fun getAllTrashed(): Flowable<List<Holder>> = holderDao.getAllTrashed()
            .flatMap(::addCounts)

    override fun getAllNotTrashed(): Flowable<List<Holder>> = holderDao.getAllNotTrashed()
            .flatMap(::addCounts)

    override fun get(id: String): Flowable<Holder> = Flowables.combineLatest(
            holderDao.get(id), cardDao.getCountByHolder(id), debtDao.getCountByHolder(id))
            .map { it.first.toDomain(it.second, it.third) }

    override fun removeTrashed(): Completable = holderDao.getAllTrashed()
            .flatMapCompletable { Completable.fromAction { holderDao.deleteAll(it) } }

    private fun addCounts(data: List<DataHolder>): Flowable<List<Holder>> = Flowable.fromIterable(data)
            .flatMapMaybe {
                Maybes.zip(Maybe.just(it),
                        cardDao.getCountByHolder(it.name).firstElement(),
                        debtDao.getCountByHolder(it.name).firstElement())
            }
            .map { it.first.toDomain(it.second, it.third) }
            .toList()
            .toFlowable()

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