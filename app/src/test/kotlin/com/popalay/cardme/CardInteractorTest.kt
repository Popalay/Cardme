package com.popalay.cardme

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.popalay.cardme.business.cards.CardInteractor
import com.popalay.cardme.business.exception.AppException
import com.popalay.cardme.data.models.Card
import com.popalay.cardme.data.models.Holder
import com.popalay.cardme.data.repositories.card.CardRepository
import com.popalay.cardme.data.repositories.holder.HolderRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import kotlin.test.assertEquals

class CardInteractorTest {

    private lateinit var cardRepository: CardRepository
    private lateinit var holderRepository: HolderRepository
    private lateinit var cardInteractor: CardInteractor

    @Before fun beforeEachTest() {
        cardRepository = mock<CardRepository>()
        holderRepository = mock<HolderRepository>()
        cardInteractor = CardInteractor(cardRepository, holderRepository)
    }

    @Test fun validateCard_Success() {
        val card = Card(number = "8876437654376548", redactedNumber = "**** **** **** 6548")

        whenever(cardRepository.cardIsNew(card)).thenReturn(Single.just(true))

        val testObserver = cardInteractor.validateCard(card).test()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertNoErrors()
                .assertValue { it === card }
                .assertComplete()
    }

    @Test fun validateCard_Failed() {
        val card = Card(number = "8876437654376548", redactedNumber = "**** **** **** 6548")

        whenever(cardRepository.cardIsNew(card)).thenReturn(Single.just(false))

        val testObserver = cardInteractor.validateCard(card).test()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertError(AppException::class.java)
                .assertTerminated()
    }

    @Test fun saveCard_Success() {
        val card = Card()

        whenever(cardRepository.save(card)).thenReturn(Single.just(card))
        whenever(holderRepository.updateCounts(Holder())).thenReturn(Completable.complete())

        val testObserver = cardInteractor.save(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).save(card)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun getCards_Success() {
        val cards = (1..5).map { Card() }.toList()

        whenever(cardRepository.getAll()).thenReturn(Flowable.just(cards))

        val testObserver = cardInteractor.getCards().test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).getAll()

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 5 }
                .assertComplete()
    }

    @Test fun getCardsByHolder_Success() {
        val cards = (1..5).map { Card() }.toList()
        val holderId = "0"

        whenever(cardRepository.getAllByHolder(holderId)).thenReturn(Flowable.just(cards))

        val testObserver = cardInteractor.getCardsByHolder(holderId).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).getAllByHolder(holderId)

        testObserver
                .assertNoErrors()
                .assertValue { it.count() == 5 }
                .assertComplete()
    }

    @Test fun updateCards_Success() {
        val cards = (1..5).map { Card(position = it * 3) }.toMutableList()
        val cardsRePositioned = (1..5).map { Card(position = it - 1) }.toMutableList()

        whenever(cardRepository.update(cards)).thenReturn(Completable.complete())

        val testObserver = cardInteractor.updateCards(cards).test()

        testObserver.awaitTerminalEvent()

        argumentCaptor<List<Card>>().apply {
            verify(cardRepository).update(capture())
            assertEquals(cardsRePositioned, firstValue)
        }

        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun removeCard_Success() {
        val card = Card()

        whenever(cardRepository.remove(card)).thenReturn(Completable.complete())

        val testObserver = cardInteractor.removeCard(card).test()

        testObserver.awaitTerminalEvent()

        verify(cardRepository).remove(card)

        testObserver
                .assertNoErrors()
                .assertComplete()
    }
}