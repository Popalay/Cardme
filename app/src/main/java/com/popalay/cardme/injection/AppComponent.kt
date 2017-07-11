package com.popalay.cardme.injection

import com.popalay.cardme.App
import com.popalay.cardme.presentation.base.navigation.CustomRouter
import com.popalay.cardme.presentation.screens.adddebt.AddDebtPresenter
import com.popalay.cardme.presentation.screens.debts.DebtsPresenter
import com.popalay.cardme.presentation.screens.home.HomePresenter
import com.popalay.cardme.presentation.screens.splash.SplashPresenter
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        UiModule::class,
        AppModule::class)
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: App): Builder
        fun build(): AppComponent
    }

    fun getRouter(): CustomRouter

    fun inject(presenter: AddDebtPresenter)

    fun inject(presenter: DebtsPresenter)

    fun inject(presenter: SplashPresenter)

    fun inject(presenter: HomePresenter)
}