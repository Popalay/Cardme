package com.popalay.cardme.data

import com.popalay.cardme.data.model.Card as DataCard
import com.popalay.cardme.data.model.Holder as DataHolder
import com.popalay.cardme.data.model.Settings as DataSettings
import com.popalay.cardme.domain.model.Holder as DomainHolder
import com.popalay.cardme.domain.model.Card as DomainCard
import com.popalay.cardme.domain.model.Settings as DomainSettings

fun DomainCard.applyMapper(
) = DataCard(number, title, redactedNumber, cardType, generatedBackgroundSeed, position, isTrash, isPending, holderName)

fun List<DomainCard>.applyMapper() = this.map(DomainCard::applyMapper)

fun DomainHolder.applyMapper() = DataHolder(name, isTrash, isPending)

fun DomainSettings.applyMapper() = DataSettings(0, language, theme, isCardBackground)