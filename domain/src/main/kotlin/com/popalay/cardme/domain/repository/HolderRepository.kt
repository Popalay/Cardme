package com.popalay.cardme.domain.repository

import com.popalay.cardme.domain.model.Holder
import io.reactivex.Completable
import io.reactivex.Flowable

interface HolderRepository {

    fun save(holder: Holder): Completable

    fun getAll(): Flowable<List<Holder>>

    fun get(holderName: String): Flowable<Holder>

    fun removeTrashed(): Completable
}