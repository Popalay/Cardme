package com.popalay.cardme.presentation.screens.adddebt

import android.arch.lifecycle.ViewModel
import com.popalay.cardme.injection.PerActivity
import com.popalay.cardme.injection.ViewModelKey
import com.popalay.cardme.presentation.base.navigation.CustomNavigator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddDebtModule {

    @Binds
    @IntoMap
    @PerActivity
    @ViewModelKey(AddDebtViewModel::class)
    abstract fun bindsAddDebtViewModel(viewModel: AddDebtViewModel): ViewModel

    @Binds
    @PerActivity
    abstract fun bindsNavigator(navigator: AddDebtNavigator): CustomNavigator

}