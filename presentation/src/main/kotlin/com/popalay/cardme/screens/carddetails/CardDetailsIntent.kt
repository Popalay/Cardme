/*
 * Created by popalay on 04.01.18 23:03
 * Copyright (c) 2018. All right reserved.
 *
 * Last modified 03.01.18 23:54
 */

package com.popalay.cardme.screens.carddetails

import com.popalay.cardme.base.mvi.Intent
import com.popalay.cardme.domain.model.Card

sealed class CardDetailsIntent : Intent {

	sealed class Initial : CardDetailsIntent() {
		data class GetCard(val number: String) : CardDetailsIntent.Initial()
		object GetHolderNames : CardDetailsIntent.Initial()
		object GetShouldShowBackground : CardDetailsIntent.Initial()
		object CheckNfc : CardDetailsIntent.Initial()
	}

	data class CardNameChanged(val card: Card) : CardDetailsIntent()
	data class CardTitleChanged(val card: Card) : CardDetailsIntent()
	data class ShareCard(val card: Card) : CardDetailsIntent()
	data class MarkAsTrash(val card: Card) : CardDetailsIntent()
	object EnterTransitionFinished : CardDetailsIntent()
}