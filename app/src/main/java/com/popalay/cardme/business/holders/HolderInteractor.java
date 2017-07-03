package com.popalay.cardme.business.holders;

import android.Manifest;
import android.content.Context;

import com.annimon.stream.Stream;
import com.github.tamir7.contacts.Contact;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.data.repositories.device.DeviceRepository;
import com.popalay.cardme.data.repositories.holder.HolderRepository;
import com.popalay.cardme.utils.PermissionChecker;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class HolderInteractor {

    private final Context context;
    private final DeviceRepository deviceRepository;
    private final HolderRepository holderRepository;

    @Inject public HolderInteractor(Context context,
            DeviceRepository deviceRepository,
            HolderRepository holderRepository) {
        this.context = context;
        this.deviceRepository = deviceRepository;
        this.holderRepository = holderRepository;
    }

    public Flowable<List<Holder>> getHolders() {
        return holderRepository.getAll()
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Holder> getHolder(String holderId) {
        return holderRepository.get(holderId)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<String> getHolderName(String holderId) {
        return getHolder(holderId)
                .map(Holder::getName)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<String>> getHolderNames() {
        return PermissionChecker.check(context, Manifest.permission.READ_CONTACTS)
                .flatMap(granted -> holderRepository.getAll(), this::transform)
                .subscribeOn(Schedulers.io());
    }

    private List<String> transform(boolean withContacts, List<Holder> holders) {
        final List<String> names = Stream.of(holders).map(Holder::getName).toList();
        if (withContacts) {
            Stream.of(deviceRepository.getContacts()).map(Contact::getDisplayName).forEach(names::add);
        }
        return Stream.of(names).distinct().sorted().toList();
    }
}
