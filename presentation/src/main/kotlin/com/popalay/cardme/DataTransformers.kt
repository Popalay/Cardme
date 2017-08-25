package com.popalay.cardme

import com.popalay.cardme.data.models.Card
import io.card.payment.CreditCard

object DataTransformers {

    fun transform(creditCard: CreditCard) = Card(
            number = creditCard.cardNumber,
            redactedNumber = creditCard.redactedCardNumber,
            cardType = when (creditCard.cardType) {
                io.card.payment.CardType.MASTERCARD -> Card.CARD_TYPE_MASTERCARD
                io.card.payment.CardType.VISA -> Card.CARD_TYPE_VISA
                io.card.payment.CardType.MAESTRO -> Card.CARD_TYPE_MAESTRO
                io.card.payment.CardType.AMEX -> Card.CARD_TYPE_AMEX
                io.card.payment.CardType.DINERSCLUB -> Card.CARD_TYPE_DINERSCLUB
                io.card.payment.CardType.DISCOVER -> Card.CARD_TYPE_DISCOVER
                io.card.payment.CardType.JCB -> Card.CARD_TYPE_JCB
                else -> Card.CARD_TYPE_UNKNOWN
            }
    )

}