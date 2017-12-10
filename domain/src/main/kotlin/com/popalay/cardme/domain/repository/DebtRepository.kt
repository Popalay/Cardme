package com.popalay.cardme.domain.repository

import com.popalay.cardme.domain.model.Debt
import io.reactivex.Flowable

interface DebtRepository : Repository<Debt, Int> {
    fun getNotTrashedByHolder(holderName: String): Flowable<List<Debt>>
}