package com.popalay.cardme.screens.holders

import android.arch.lifecycle.ViewModel
import com.alexfacciorusso.daggerviewmodel.ViewModelKey
import com.popalay.cardme.injection.PerFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HoldersModule {

    @Binds
    @IntoMap
    @PerFragment
    @ViewModelKey(HoldersViewModel::class)
    abstract fun bindsHoldersViewModel(viewModel: HoldersViewModel): ViewModel
}
