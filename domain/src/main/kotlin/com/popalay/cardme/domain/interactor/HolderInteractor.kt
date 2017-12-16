package com.popalay.cardme.domain.interactor

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

    fun save(holder: Holder): Completable = holderRepository.save(holder)
            .subscribeOn(Schedulers.io())

    //TODO Implement Pending save
    fun savePending(holder: Holder): Completable = holderRepository.save(holder)
            .subscribeOn(Schedulers.io())

    fun get(holderName: String): Flowable<Holder> = holderRepository.get(holderName)
            .subscribeOn(Schedulers.io())

    fun getAll(): Flowable<List<Holder>> = holderRepository.getAllNotTrashed()
            .subscribeOn(Schedulers.io())

    private fun transform(holders: List<Holder>, contacts: List<String>): List<String> {
        val names = holders.map { it.name }.toMutableList()
        names.addAll(contacts)
        return names.filter(String::isNotBlank).distinct().sorted()
    }

    fun getNames(): Flowable<List<String>> = Flowables.combineLatest(
            holderRepository.getAll(),
            deviceRepository.getContactsNames().toFlowable(),
            this::transform)
            .subscribeOn(Schedulers.io())
}
