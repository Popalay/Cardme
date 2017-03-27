package com.popalay.yocard.data.repositories;

import android.content.Context;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.data.models.Holder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;

@Singleton
public class DebtsRepository {

    private final Context context;

    @Inject
    public DebtsRepository(Context context) {
        this.context = context;
    }

    public Completable save(Debt debt) {
        return Completable.fromAction(() -> RxRealm.generateObjectId(debt, (realm, id) -> {
            debt.setId(id);
            Holder realmHolder = realm.where(Holder.class).equalTo(Holder.NAME, debt.getHolder().getName())
                    .findFirst();
            if (realmHolder != null) {
                debt.setHolder(realmHolder);
            } else {
                Number num = realm.where(Holder.class).max(Holder.ID);
                long nextID = num != null ? num.longValue() + 1L : 0L;
                debt.getHolder().setId(nextID);
            }
            debt.getHolder().setDebtCount(debt.getHolder().getDebtCount() + 1);
            if (debt.getCreatedAt() == 0L) {
                debt.setCreatedAt(System.currentTimeMillis());
            }
            realm.copyToRealmOrUpdate(debt);
        }));
    }

    public Observable<List<Debt>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Debt.class)
                .findAllSorted(Debt.CREATED_AT));
    }

    public Observable<List<Debt>> getAllByHolder(long holderId) {
        return RxRealm.listenList(realm -> realm.where(Debt.class)
                .equalTo(Debt.HOLDER_ID, holderId)
                .findAllSorted(Debt.CREATED_AT));
    }

    public Observable<Holder> get(long id) {
        return RxRealm.listenElement(realm -> realm.where(Holder.class)
                .equalTo(Debt.ID, id)
                .findAll());
    }

    public Completable remove(Debt debt) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm ->
                realm.where(Debt.class).equalTo(Debt.ID, debt.getId()).findAll().deleteAllFromRealm()));
    }
}
