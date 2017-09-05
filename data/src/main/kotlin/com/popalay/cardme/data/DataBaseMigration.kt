package com.popalay.cardme.data

import io.realm.DynamicRealm
import io.realm.RealmMigration

class DataBaseMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var updatedVersion = oldVersion

        if (updatedVersion == 0L) {
            schema.get("DataCard").addField("isTrash", Boolean::class.java)
            schema.get("DataHolder").addField("isTrash", Boolean::class.java)
            schema.get("DataDebt").addField("isTrash", Boolean::class.java)
            updatedVersion++
        }

        if (updatedVersion == 1L) {
            schema.get("DataHolder")
                    .removePrimaryKey()
                    .removeField("id")
                    .addPrimaryKey("name")
            schema.get("DataCard")
                    .removePrimaryKey()
                    .removeField("id")
                    .addPrimaryKey("number")
            updatedVersion++
        }

        if (updatedVersion == 2L) {
            schema.get("DataHolder")
                    .removeField("cardsCount")
                    .removeField("debtCount")
                    .addRealmListField("cards", schema.get("DataCard"))
                    .addRealmListField("debts", schema.get("DataDebt"))
            updatedVersion++
        }

        if (updatedVersion == 3L) {
            updatedVersion++
        }
    }
}