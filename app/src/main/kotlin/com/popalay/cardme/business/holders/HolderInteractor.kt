package com.popalay.cardme.business.holders

import android.Manifest
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.data.repositories.device.DeviceRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolderInteractor @Inject constructor(
        private val deviceRepository: DeviceRepository,
        private val holderRepository: HolderRepository
) {

    fun getHolders(): Flowable<List<Holder>> = holderRepository.getAll()
            .subscribeOn(Schedulers.io())

    fun getHolder(holderId: String): Flowable<Holder> = holderRepository.get(holderId)
            .subscribeOn(Schedulers.io())

    fun getFavoriteHolder(): Maybe<Holder> = holderRepository.getWithMaxCounters()
            .subscribeOn(Schedulers.io())

    fun getHolderName(holderId: String): Flowable<String> = getHolder(holderId)
            .map { it.name }
            .subscribeOn(Schedulers.io())

    fun getHolderNames(): Flowable<List<String>>
            = deviceRepository.checkPermissions(Manifest.permission.READ_CONTACTS)
            .flatMap({ holderRepository.getAll() }, this::transform)
            .subscribeOn(Schedulers.io())

    private fun transform(withContacts: Boolean, holders: List<Holder>): List<String> {
        val names = holders.map { it.name }.toMutableList()
        if (withContacts) names.addAll(deviceRepository.getContacts().map { it?.displayName ?: "" })
        return names.filter { it.isNotBlank() }.distinct().sorted()
    }
}
