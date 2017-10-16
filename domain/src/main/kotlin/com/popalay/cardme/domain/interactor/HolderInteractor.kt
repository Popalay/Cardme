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

    fun save(holder: Holder): Completable = holderRepository.save(holder.apply { isPending = false })
            .subscribeOn(Schedulers.io())

    fun savePending(holder: Holder): Completable = holderRepository.save(holder.apply { isPending = true })
            .subscribeOn(Schedulers.io())

    fun get(holderName: String): Flowable<Holder> = holderRepository.get(holderName)
            .subscribeOn(Schedulers.io())

    fun addCard(holderName: String, card: Card): Completable = Completable.complete()/*holderRepository.addCard(holderName, card)
            .subscribeOn(Schedulers.io())*/

    fun addCard(card: Card): Completable = addCard(card.holderName, card)
            .subscribeOn(Schedulers.io())

    fun addDebt(holderName: String, debt: Debt): Completable = Completable.complete()/*holderRepository.addDebt(holderName, debt)
            .subscribeOn(Schedulers.io())*/

    fun getAll(): Flowable<List<Holder>> = holderRepository.getAllNotTrashed()
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
