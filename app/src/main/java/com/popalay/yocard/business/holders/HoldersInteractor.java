package com.popalay.yocard.business.holders;

import com.popalay.yocard.data.models.Holder;
import com.popalay.yocard.data.repositories.HolderRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class HoldersInteractor {

    private final HolderRepository holderRepository;

    @Inject
    public HoldersInteractor(HolderRepository holderRepository) {
        this.holderRepository = holderRepository;
    }

    public Observable<List<Holder>> getHolders() {
        return holderRepository.getHolders()
                .subscribeOn(Schedulers.io());
    }
}
