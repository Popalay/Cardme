package com.popalay.yocard;

import android.app.Application;

import com.popalay.yocard.injection.AppComponent;
import com.popalay.yocard.injection.AppModule;
import com.popalay.yocard.injection.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private static App app;

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Realm.init(this);

        final RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static AppComponent appComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .build();
        }
        return appComponent;
    }
}