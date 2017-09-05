package com.popalay.cardme.domain.repository

import com.github.tamir7.contacts.Contact
import io.reactivex.Flowable

interface DeviceRepository {

    fun supportNfc(): Boolean

    fun getContacts(): List<Contact>

    fun checkPermissions(vararg permissions: String): Flowable<Boolean>

}
