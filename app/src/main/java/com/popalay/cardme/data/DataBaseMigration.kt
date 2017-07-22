package com.popalay.cardme.data

import io.realm.DynamicRealm
import io.realm.RealmMigration

class DataBaseMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var version = oldVersion

        if (version == 0L) {
            schema.get("Card").addField("isTrash", Boolean::class.java)
            version++
        }

        if (version == 1L) {
            schema.get("Holder").addField("isTrash", Boolean::class.java)
            schema.get("Debt").addField("isTrash", Boolean::class.java)
            version++
        }
    }
}