package com.popalay.cardme.data.repositories;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DeviceRepository {

    @Inject
    public DeviceRepository() {
    }

    public List<Contact> getContacts(){
        return Contacts.getQuery().find();
    }
}
