package com.popalay.cardme.data.repository.device

import android.content.Context
import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactProvider @Inject constructor(context: Context) {

    init {
        Contacts.initialize(context)
    }

    fun getContacts(): List<Contact> = Contacts.getQuery().find()

}