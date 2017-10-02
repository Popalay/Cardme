package com.popalay.cardme.base


import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
        private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        creator ?: creators.keys.filter(modelClass::isAssignableFrom).firstOrNull()?.apply { creator = creators[this] }
        creator ?: throw IllegalArgumentException("unknown model class " + modelClass)

        return creator?.get() as T
    }
}