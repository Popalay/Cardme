package com.popalay.cardme.data.repositories;

import com.popalay.cardme.data.models.Holder;

import java.util.List;

import rx.Completable;
import rx.Observable;

public interface IHolderRepository {

    Observable<List<Holder>> getAll();

    Observable<Holder> get(long holderId);

    Completable updateCounts(Holder holder);

    Completable remove(Holder holder);
}
