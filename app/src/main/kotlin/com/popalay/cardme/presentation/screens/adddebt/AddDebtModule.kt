package com.popalay.cardme.presentation.screens.adddebt

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddDebtModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddDebtViewModel::class)
    abstract fun bindsAddDebtViewModel(viewModel: AddDebtViewModel): ViewModel

}