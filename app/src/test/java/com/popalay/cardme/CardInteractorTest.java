package com.popalay.cardme;

import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.repositories.card.CardRepository;
import com.popalay.cardme.data.repositories.holder.HolderRepository;

import org.junit.Before;
import org.junit.Test;

import io.card.payment.CreditCard;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardInteractorTest {

    private CardRepository cardRepository;
    private HolderRepository holderRepository;
    private CardInteractor cardInteractor;

    @Before
    public void beforeEachTest() {
        cardRepository = mock(CardRepository.class);
        cardInteractor = new CardInteractor(cardRepository, holderRepository);
    }

    @Test
    public void transformCard_Success() {
        final CreditCard creditCard = new CreditCard("8876437654376548", 12, 2018, "234", null, null);
        final Card card = new Card(creditCard);
        when(cardRepository.cardIsNew(card)).thenReturn(Single.just(true));

        final TestObserver<Card> testObserver =
                cardInteractor.transformCard(creditCard).test();

        testObserver.awaitTerminalEvent();

        testObserver
                .assertNoErrors()
                .assertValue(result -> result.getNumber().equals(creditCard.getFormattedCardNumber())
                        && result.getRedactedNumber().equals(creditCard.getRedactedCardNumber()));
    }
}