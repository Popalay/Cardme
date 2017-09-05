package com.popalay.cardme.domain.repository

import io.reactivex.Single

interface DeviceRepository {

    fun supportNfc(): Boolean

    fun getContactsNames(): Single<List<String>>

}
