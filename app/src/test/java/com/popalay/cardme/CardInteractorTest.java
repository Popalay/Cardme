package com.popalay.cardme;

import com.popalay.cardme.business.cards.CardInteractor;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.data.repositories.card.CardRepository;
import com.popalay.cardme.data.repositories.holder.HolderRepository;

import org.junit.Before;
import org.junit.Test;

import io.card.payment.CreditCard;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(cardRepository.isCardExist(card)).thenReturn(Single.just(false));

        final TestSubscriber<Card> testSubscriber = TestSubscriber.create();
        // call method and get result
        cardInteractor.transformCard(creditCard).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        // test no errors was not occurred
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();

        final Card result = testSubscriber.getOnNextEvents().get(0);
        assertThat(result.getNumber()).isEqualTo(creditCard.getFormattedCardNumber());
        assertThat(result.getRedactedNumber()).isEqualTo(creditCard.getRedactedCardNumber());
    }

}
