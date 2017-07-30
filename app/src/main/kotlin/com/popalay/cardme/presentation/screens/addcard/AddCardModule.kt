package com.popalay.cardme.presentation.screens.addcard

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddCardModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddCardViewModel::class)
    abstract fun bindsAddCardViewModel(addCardViewModel: AddCardViewModel): ViewModel

}