package com.popalay.cardme.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface Repository<D, in I> {

    fun save(data: D): Completable

    fun markAsTrash(data: D): Completable

    fun removeTrashed(): Completable

    fun restore(data: D): Completable

    fun get(id: I): Flowable<D>

    fun contains(id: I): Single<Boolean>

    fun getAll(): Flowable<List<D>>

    fun getAllTrashed(): Flowable<List<D>>

    fun getAllNotTrashed(): Flowable<List<D>>

}