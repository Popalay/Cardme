package com.popalay.cardme.screens.addcard

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AddCardModule {

    @Binds @IntoMap @PerActivity
    @ViewModelKey(AddCardViewModel::class)
    fun bindsAddCardViewModel(addCardViewModel: AddCardViewModel): ViewModel
}