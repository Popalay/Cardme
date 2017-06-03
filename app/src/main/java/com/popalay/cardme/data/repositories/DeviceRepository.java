package com.popalay.cardme.data.repositories;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.util.List;

public class DeviceRepository implements IDeviceRepository {

    @Override public List<Contact> getContacts(){
        return Contacts.getQuery().find();
    }
}
