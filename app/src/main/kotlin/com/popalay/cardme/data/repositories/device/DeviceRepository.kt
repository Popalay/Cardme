package com.popalay.cardme.data.repositories.device

import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor() {

    fun getContacts(): List<Contact> = Contacts.getQuery().find()
}
