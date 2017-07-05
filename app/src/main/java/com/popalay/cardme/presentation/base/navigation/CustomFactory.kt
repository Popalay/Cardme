package com.popalay.cardme.presentation.base.navigation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.popalay.cardme.App
import com.popalay.cardme.presentation.screens.addcard.AddCardViewModel
import com.popalay.cardme.presentation.screens.holderdetails.HolderDetailsViewModel
import io.card.payment.CreditCard

import java.lang.reflect.InvocationTargetException

class CustomFactory(
        private val application: App,
        private val navigationExtras: NavigationExtrasHolder
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            if (modelClass.isAssignableFrom(HolderDetailsViewModel::class.java)) {
                return modelClass.getConstructor(Application::class.java, String::class.java)
                        .newInstance(application, navigationExtras.holderId).also {
                    navigationExtras.holderId = null
                }
            } else if (modelClass.isAssignableFrom(AddCardViewModel::class.java)) {
                return modelClass.getConstructor(Application::class.java, CreditCard::class.java)
                        .newInstance(application, navigationExtras.creditCard).also {
                    navigationExtras.creditCard = null
                }
            } else if (AndroidViewModel::class.java.isAssignableFrom(modelClass)) {
                return modelClass.getConstructor(Application::class.java).newInstance(application)
            }
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of " + modelClass, e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of " + modelClass, e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of " + modelClass, e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of " + modelClass, e)
        }

        return super.create(modelClass)
    }
}