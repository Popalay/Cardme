package com.popalay.cardme.injection

import com.popalay.cardme.App
import com.popalay.cardme.base.navigation.CustomRouter
import com.popalay.cardme.data.injection.DataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        UiModule::class,
        DataModule::class,
        AppModule::class)
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun getRouter(): CustomRouter

}