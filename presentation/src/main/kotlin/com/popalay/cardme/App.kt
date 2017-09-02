package com.popalay.cardme

import com.facebook.stetho.Stetho
import com.popalay.cardme.injection.AppComponent
import com.popalay.cardme.injection.DaggerAppComponent
import com.popalay.cardme.injection.applyAutoInjector
import com.squareup.leakcanary.LeakCanary
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import dagger.android.support.DaggerApplication
import io.realm.Realm
import io.realm.RealmConfiguration
import shortbread.Shortbread

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        app = this

        applyAutoInjector()
        LeakCanary.install(this)
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .schemaVersion(BuildConfig.DATABASE_VERSION)
                .deleteRealmIfMigrationNeeded()
                //.migration(DataBaseMigration())
                .build()
        Realm.setDefaultConfiguration(config)
        Shortbread.create(this)

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build())
    }

    override fun applicationInjector() = DaggerAppComponent.builder()
            .application(this)
            .build().apply { appComponent = this }

    companion object {

        lateinit var app: App
        @JvmStatic lateinit var appComponent: AppComponent

    }
}