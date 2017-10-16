package com.popalay.cardme.domain.repository

import com.popalay.cardme.domain.model.Card
import io.reactivex.Completable
import io.reactivex.Single

interface CardRepository : Repository<Card, String> {

    fun update(data: List<Card>): Completable

    fun toJson(card: Card): Single<String>

    fun fromJson(source: String): Single<Card>
}
