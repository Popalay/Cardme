package com.popalay.cardme.screens.debts

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.PerFragment
import com.popalay.cardme.injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DebtsModule {

    @Binds
    @IntoMap
    @PerFragment
    @ViewModelKey(DebtsViewModel::class)
    abstract fun bindsDebtsViewModel(viewModel: DebtsViewModel): ViewModel

}