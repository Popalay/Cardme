package com.popalay.cardme

import android.app.Application

import com.facebook.stetho.Stetho
import com.github.tamir7.contacts.Contacts
import com.popalay.cardme.injection.AppComponent
import com.popalay.cardme.injection.AppModule
import com.popalay.cardme.injection.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import com.uphyca.stetho_realm.RealmInspectorModulesProvider

import io.realm.Realm
import io.realm.RealmConfiguration
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import shortbread.Shortbread

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        app = this
        LeakCanary.install(this)
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        Contacts.initialize(this)
        Shortbread.create(this)

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())
    }

    companion object {

        private lateinit var app: App
        private val cicerone: Cicerone<Router> by lazy { Cicerone.create(); }

        val appComponent: AppComponent by lazy {
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
        }

        fun getRouter() = App.cicerone.router
        fun getNavigatorHolder() = App.cicerone.navigatorHolder

    }
}