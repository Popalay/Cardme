package com.popalay.cardme.data

import com.popalay.cardme.data.model.Card as DataCard
import com.popalay.cardme.data.model.Debt as DataDebt
import com.popalay.cardme.data.model.Holder as DataHolder
import com.popalay.cardme.data.model.Settings as DataSettings
import com.popalay.cardme.domain.model.Card as DomainCard
import com.popalay.cardme.domain.model.Debt as DomainDebt
import com.popalay.cardme.domain.model.Holder as DomainHolder
import com.popalay.cardme.domain.model.Settings as DomainSettings

fun DomainCard.toData() = DataCard(
        number,
        title,
        redactedNumber,
        cardType,
        generatedBackgroundSeed,
        position,
        isTrash,
        isPending,
        holderName
)

fun DataCard.toDomain() = DomainCard(
        number,
        title,
        redactedNumber,
        cardType,
        generatedBackgroundSeed,
        position,
        isTrash,
        isPending,
        holderName
)

fun DomainHolder.toData() = DataHolder(name, isTrash)

fun DataHolder.toDomain(cardCount: Int = 0, debtsCount: Int = 0) = DomainHolder(name, isTrash, cardCount, debtsCount)

fun DomainSettings.toData() = DataSettings(0, language, theme, isCardBackground)

fun DataSettings.toDomain() = DomainSettings(language, theme, isCardBackground)

fun DomainDebt.toData() = DataDebt(id, message, createdAt, isTrash, holderName)

fun DataDebt.toDomain() = DomainDebt(id, message, createdAt, isTrash, holderName)