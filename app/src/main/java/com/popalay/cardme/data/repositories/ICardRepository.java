package com.popalay.cardme.data.repositories;

import com.popalay.cardme.data.models.Card;

import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface ICardRepository {

    Completable save(Card card);

    Completable update(List<Card> cards);

    Observable<List<Card>> getAll();

    Observable<List<Card>> getAllByHolder(long holderId);

    Completable remove(Card card);

    Single<Card> getByFormattedNumber(String formattedNumber);
}
