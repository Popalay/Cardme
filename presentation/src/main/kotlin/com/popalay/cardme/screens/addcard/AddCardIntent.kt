/*
 * Created by popalay on 30.12.17 22:10
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 22:10
 */

package com.popalay.cardme.screens.addcard

import com.popalay.cardme.base.mvi.Intent

sealed class AddCardIntent : Intent {
    data class InitialGetCard(val number: String) : AddCardIntent()
    object InitialGetHolderNames : AddCardIntent()
    object InitialGetShoulsShowBackground: AddCardIntent()
    data class NameChanged(val name: String) : AddCardIntent()
    data class TitleChanged(val title: String) : AddCardIntent()
    object Accept : AddCardIntent()
}