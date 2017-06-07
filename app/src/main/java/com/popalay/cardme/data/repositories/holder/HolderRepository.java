package com.popalay.cardme.data.repositories.holder;

import com.popalay.cardme.data.models.Holder;

import java.util.List;

import rx.Completable;
import rx.Observable;

public interface HolderRepository {

    Observable<List<Holder>> getAll();

    Observable<Holder> get(long holderId);

    Completable updateCounts(Holder holder);

    Completable remove(Holder holder);
}
