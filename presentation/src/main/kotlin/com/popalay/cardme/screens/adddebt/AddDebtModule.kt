package com.popalay.cardme.screens.adddebt

import android.arch.lifecycle.ViewModel
import com.alexfacciorusso.daggerviewmodel.ViewModelKey
import com.popalay.cardme.base.navigation.CustomNavigator
import com.popalay.cardme.injection.PerActivity
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