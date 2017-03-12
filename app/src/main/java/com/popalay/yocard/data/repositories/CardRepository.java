package com.popalay.yocard.data.repositories;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.MainThread;

import com.github.popalay.rxrealm.RxRealm;
import com.popalay.yocard.R;
import com.popalay.yocard.data.models.Card;
import com.popalay.yocard.data.models.Holder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Sort;
import rx.Completable;
import rx.Observable;

@Singleton
public class CardRepository {

    private final Context context;

    @Inject
    public CardRepository(Context context) {
        this.context = context;
    }

    public void save(Card card) {
        RxRealm.generateObjectId(card, (realm, id) -> {
            card.setId(id);
            Holder realmHolder = realm.where(Holder.class).equalTo(Holder.NAME, card.getHolder().getName())
                    .findFirst();
            if (realmHolder != null) {
                card.setHolder(realmHolder);
            } else {
                Number num = realm.where(Holder.class).max(Holder.ID);
                long nextID = num != null ? num.longValue() + 1L : 0L;
                card.getHolder().setId(nextID);
            }
            card.getHolder().setCardsCount(card.getHolder().getCardsCount() + 1);
            realm.copyToRealmOrUpdate(card);
        });
    }

    public Observable<List<Card>> getCards() {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.USAGE, Sort.DESCENDING));
    }

    public Observable<List<Card>> getHolderCards(long holderId) {
        return RxRealm.listenList(realm -> realm.where(Card.class)
                .equalTo(Card.HOLDER_ID, holderId)
                .findAllSorted(Card.ID, Sort.DESCENDING)
                .sort(Card.USAGE, Sort.DESCENDING));
    }

    @MainThread
    public Completable copyToClipboard(Card card) {
        return Completable.fromAction(() -> {
            final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            final ClipData clip = ClipData.newPlainText(context.getString(R.string.card_holder_number,
                    card.getHolder().getName()), card.getNumber());
            clipboard.setPrimaryClip(clip);
        });
    }

    public Completable incCardUsage(final Card card) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            card.setUsage(card.getUsage() + 1);
            realm.copyToRealmOrUpdate(card);
        }));
    }

    public Completable removeCard(final Card card) {
        return Completable.fromAction(() -> RxRealm.doTransactional(realm -> {
            realm.where(Card.class).equalTo(Card.ID, card.getId()).findAll().deleteAllFromRealm();
        }));
    }
}
