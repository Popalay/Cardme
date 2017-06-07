package com.popalay.cardme.data.repositories.device;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.util.List;

public class DefaultDeviceRepository implements DeviceRepository {

    @Override public List<Contact> getContacts(){
        return Contacts.getQuery().find();
    }
}
