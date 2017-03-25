package com.popalay.yocard.data.repositories;

import android.content.Context;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.models.Debt;
import com.popalay.yocard.data.models.Holder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Sort;
import rx.Completable;
import rx.Observable;

@Singleton
public class HolderRepository {

    private final Context context;

    @Inject
    public HolderRepository(Context context) {
        this.context = context;
    }

    public Observable<List<Holder>> getAll() {
        return RxRealm.listenList(realm -> realm.where(Holder.class)
                .findAllSorted(Holder.NAME)
                .sort(Holder.DEBTS_COUNT, Sort.DESCENDING, Holder.CARDS_COUNT, Sort.DESCENDING));
    }

    public Observable<Holder> get(long holderId) {
        return RxRealm.listenElement(realm -> realm.where(Holder.class)
                .equalTo(Holder.ID, holderId)
                .findAll());
    }

    public Completable updateCounts(Holder holder) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            final long cardCount = realm.where(Card.class).equalTo(Card.HOLDER_ID, holder.getId()).count();
            final long debtCount = realm.where(Debt.class).equalTo(Debt.HOLDER_ID, holder.getId()).count();
            if (cardCount <= 0 && debtCount <= 0) {
                realm.where(Holder.class).equalTo(Holder.ID, holder.getId()).findAll().deleteAllFromRealm();
            } else {
                holder.setCardsCount((int) cardCount);
                holder.setDebtCount((int) debtCount);
                realm.copyToRealmOrUpdate(holder);
            }
        }));
    }

    public Completable remove(Holder holder) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            realm.where(Holder.class).equalTo(Holder.ID, holder.getId()).findAll().deleteAllFromRealm();
        }));
    }
}
