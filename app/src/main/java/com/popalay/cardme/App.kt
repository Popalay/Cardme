package com.popalay.cardme

import com.facebook.stetho.Stetho
import com.github.tamir7.contacts.Contacts
import com.popalay.cardme.injection.AppComponent
import com.popalay.cardme.injection.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.realm.Realm
import io.realm.RealmConfiguration
import shortbread.Shortbread

class App : DaggerApplication() {

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

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder()
            .application(this)
            .build().apply { appComponent = this }

    companion object {

        lateinit var app: App
        @JvmStatic lateinit var appComponent: AppComponent

    }
}