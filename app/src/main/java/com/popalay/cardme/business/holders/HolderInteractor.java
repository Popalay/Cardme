package com.popalay.cardme.business.holders;

import android.Manifest;
import android.content.Context;

import com.annimon.stream.Stream;
import com.github.tamir7.contacts.Contact;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.data.repositories.DeviceRepository;
import com.popalay.cardme.data.repositories.HolderRepository;
import com.popalay.cardme.utils.PermissionChecker;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class HolderInteractor {

    private final Context context;
    private final DeviceRepository deviceRepository;
    private final HolderRepository holderRepository;

    @Inject
    public HolderInteractor(Context context,
            DeviceRepository deviceRepository,
            HolderRepository holderRepository) {
        this.context = context;
        this.deviceRepository = deviceRepository;
        this.holderRepository = holderRepository;
    }

    public Observable<List<Holder>> getHolders() {
        return holderRepository.getAll()
                .subscribeOn(Schedulers.io());
    }

    public Observable<Holder> getHolder(long holderId) {
        return holderRepository.get(holderId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<String> getHolderName(long holderId) {
        return getHolder(holderId)
                .map(Holder::getName)
                .subscribeOn(Schedulers.io());
    }

    //TODO simplify
    public Observable<List<String>> getHolderNames() {
        return PermissionChecker.check(context, Manifest.permission.READ_CONTACTS)
                .flatMap(granted -> holderRepository.getAll(), (granted, holders) -> transform(holders, granted))
                .subscribeOn(Schedulers.io());
    }

    private List<String> transform(List<Holder> holders, boolean withContacts) {
        final List<String> names = Stream.of(holders).map(Holder::getName).toList();
        if (withContacts) {
            Stream.of(deviceRepository.getContacts()).map(Contact::getDisplayName).forEach(names::add);
        }
        Collections.sort(names);
        return names;
    }
}
