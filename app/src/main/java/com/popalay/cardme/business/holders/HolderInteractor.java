package com.popalay.cardme.business.holders;

import com.annimon.stream.Stream;
import com.popalay.cardme.data.models.Holder;
import com.popalay.cardme.data.repositories.HolderRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class HolderInteractor {

    private final HolderRepository holderRepository;

    @Inject
    public HolderInteractor(HolderRepository holderRepository) {
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

    public Observable<List<String>> getHolderNames() {
        return holderRepository.getAll()
                .map(this::transform)
                .subscribeOn(Schedulers.io());
    }

    private List<String> transform(List<Holder> holders) {
        final List<String> names = Stream.of(holders).map(Holder::getName).toList();
        //Stream.of(Contacts.getQuery().find()).map(Contact::getDisplayName).forEach(names::add);
        Collections.sort(names);
        return names;
    }
}
