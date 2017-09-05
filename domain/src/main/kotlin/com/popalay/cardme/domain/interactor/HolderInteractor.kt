package com.popalay.cardme.domain.interactor

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.domain.repository.DeviceRepository
import com.popalay.cardme.domain.repository.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolderInteractor @Inject constructor(
        private val deviceRepository: DeviceRepository,
        private val holderRepository: HolderRepository
) {

    fun get(holderName: String): Flowable<Holder> = holderRepository.get(holderName)
            .subscribeOn(Schedulers.io())

    fun addCard(holderName: String, card: Card): Completable = holderRepository.addCard(holderName, card)
            .subscribeOn(Schedulers.io())

    fun addCard(card: Card): Completable = addCard(card.holder.name, card)
            .subscribeOn(Schedulers.io())

    fun addDebt(holderName: String, debt: Debt): Completable = holderRepository.addDebt(holderName, debt)
            .subscribeOn(Schedulers.io())

    fun addDebt(debt: Debt): Completable = addDebt(debt.holder.name, debt)
            .subscribeOn(Schedulers.io())

    fun getAll(): Flowable<List<Holder>> = holderRepository.getAll()
            .subscribeOn(Schedulers.io())

    fun getNames(): Flowable<List<String>>
            = Flowables.combineLatest(holderRepository.getAll(),
            deviceRepository.getContactsNames().toFlowable(), this::transform)
            .subscribeOn(Schedulers.io())

    private fun transform(holders: List<Holder>, contacts: List<String>): List<String> {
        val names = holders.map { it.name }.toMutableList()
        names.addAll(contacts)
        return names.filter(String::isNotBlank).distinct().sorted()
    }
}