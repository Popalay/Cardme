package com.popalay.yocard.data.repositories;

import android.content.Context;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.yocard.data.models.Holder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;

@Singleton
public class HolderRepository {

    private final Context context;

    @Inject
    public HolderRepository(Context context) {
        this.context = context;
    }

    public Observable<List<Holder>> getHolders() {
        return RxRealm.listenList(realm -> realm.where(Holder.class)
                .findAllSorted(Holder.NAME));
    }

    public Observable<Holder> getHolder(long holderId) {
        return RxRealm.listenElement(realm -> realm.where(Holder.class)
                .equalTo(Holder.ID, holderId)
                .findAll());
    }

    public Completable removeHolder(Holder holder, int cards) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            final Holder first = realm.where(Holder.class).equalTo(Holder.ID, holder.getId()).findFirst();
            if (cards <= 0) {
                first.deleteFromRealm();
            } else {
                first.setCardsCount(cards);
            }
        }));
    }
}
