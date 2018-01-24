package com.popalay.cardme

import com.facebook.stetho.Stetho
import com.popalay.cardme.injection.AppComponent
import com.popalay.cardme.injection.DaggerAppComponent
import com.popalay.cardme.injection.applyAutoInjector
import com.squareup.leakcanary.LeakCanary
import dagger.android.support.DaggerApplication
import shortbread.Shortbread

class App : DaggerApplication() {

    companion object {

        @JvmStatic lateinit var appComponent: AppComponent

    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        applyAutoInjector()
        initializeDevelopmentTools()
    }

    override fun applicationInjector() = DaggerAppComponent.builder()
            .application(this)
            .build().apply { appComponent = this }

    private fun initializeDevelopmentTools() {
        LeakCanary.install(this)
        Shortbread.create(this)

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .build())
    }
}