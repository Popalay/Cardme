package com.popalay.cardme.injection

import android.content.Context
import com.popalay.cardme.App
import com.popalay.cardme.base.navigation.CustomRouter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Singleton

@Module
class AppModule {

    private val cicerone = Cicerone.create(CustomRouter())

    @Provides
    fun provideApplicationContext(app: App): Context = app.applicationContext

    @Provides @Singleton
    fun provideRouter(): CustomRouter = cicerone.router

    @Provides @Singleton
    fun provideNavigatorHolder(): NavigatorHolder = cicerone.navigatorHolder

}