package com.popalay.cardme.data.repository

import com.popalay.cardme.data.dao.DebtDao
import com.popalay.cardme.data.toData
import com.popalay.cardme.data.toDomain
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.repository.DebtRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataDebtRepository @Inject constructor(
        private val debtDao: DebtDao
) : DebtRepository {

    override fun save(data: Debt): Completable = Completable.fromAction {
        debtDao.insertOrUpdate(data.toData())
    }

    override fun contains(id: Int): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(id: Int): Flowable<Debt> = debtDao.get(id)
            .map { it.toDomain() }

    override fun getAll(): Flowable<List<Debt>> = debtDao.getAll()
            .map { it.map { it.toDomain() } }

    override fun getAllTrashed(): Flowable<List<Debt>> = debtDao.getAllTrashed()
            .map { it.map { it.toDomain() } }

    override fun getAllNotTrashed(): Flowable<List<Debt>> = debtDao.getAllNotTrashed()
            .map { it.map { it.toDomain() } }

    override fun markAsTrash(data: Debt): Completable = Completable.fromAction {
        debtDao.insertOrUpdate(data.toData().apply { isTrash = true })
    }

    override fun restore(data: Debt): Completable = Completable.fromAction {
        debtDao.insertOrUpdate(data.toData().apply { isTrash = false })
    }

    override fun removeTrashed(): Completable = debtDao.getAllTrashed()
            .flatMapCompletable { Completable.fromAction { debtDao.deleteAll(it) } }
}
