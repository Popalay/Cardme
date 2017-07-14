package com.popalay.cardme

import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.repositories.card.CardRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CardInteractorTest {

    private lateinit var cardRepository: CardRepository
    private lateinit var holderRepository: HolderRepository
    private lateinit var cardInteractor: CardInteractor


    @Before fun beforeEachTest() {
        cardRepository = mock(CardRepository::class.java)
        holderRepository = mock(HolderRepository::class.java)
        cardInteractor = CardInteractor(cardRepository, holderRepository)
    }

    @Test fun validateCard_Success() {
        val card = Card(number = "8876437654376548", redactedNumber = "**** **** **** 6548")
        `when`(cardRepository.cardIsNew(card)).thenReturn(Single.just(true))
        val testObserver = cardInteractor.validateCard(card).test()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertNoErrors()
                .assertValue { it === card }
                .assertComplete()
    }

    @Test fun validateCard_Failed() {
        val card = Card(number = "8876437654376548", redactedNumber = "**** **** **** 6548")
        `when`(cardRepository.cardIsNew(card)).thenReturn(Single.just(false))
        val testObserver = cardInteractor.validateCard(card).test()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertError(AppException::class.java)
                .assertTerminated()
    }
}