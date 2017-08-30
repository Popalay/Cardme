package com.popalay.cardme.domain.interactor

import android.Manifest
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Debt
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.data.repositories.HolderRepository
import com.popalay.cardme.data.repositories.device.DeviceRepository
import io.reactivex.Completable
import io.reactivex.Flowable
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
            = deviceRepository.checkPermissions(Manifest.permission.READ_CONTACTS)

            .flatMap({ holderRepository.getAll() }, this::transform)
            .subscribeOn(Schedulers.io())

    private fun transform(withContacts: Boolean, holders: List<Holder>): List<String> {
        val names = holders.map { it.name }.toMutableList()
        if (withContacts) names.addAll(deviceRepository.getContacts().map { it.displayName })
        return names.filter { it.isNotBlank() }.distinct().sorted()
    }
}
