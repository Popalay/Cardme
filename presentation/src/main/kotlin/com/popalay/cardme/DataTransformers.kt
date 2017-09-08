package com.popalay.cardme

import com.popalay.cardme.domain.model.Card
import io.card.payment.CreditCard

object DataTransformers {

    fun transform(creditCard: CreditCard) = Card(
            number = creditCard.cardNumber,
            redactedNumber = creditCard.redactedCardNumber,
            cardType = 0/*when (creditCard.cardType) {
                CardType.MASTERCARD -> Card.CARD_TYPE_MASTERCARD
                CardType.VISA -> Card.CARD_TYPE_VISA
                CardType.MAESTRO -> Card.CARD_TYPE_MAESTRO
                CardType.AMEX -> Card.CARD_TYPE_AMEX
                CardType.DINERSCLUB -> Card.CARD_TYPE_DINERSCLUB
                CardType.DISCOVER -> Card.CARD_TYPE_DISCOVER
                CardType.JCB -> Card.CARD_TYPE_JCB
                else -> Card.CARD_TYPE_UNKNOWN
            }*/
    )

}