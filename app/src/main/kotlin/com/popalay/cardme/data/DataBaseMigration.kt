package com.popalay.cardme.data

import io.realm.DynamicRealm
import io.realm.RealmMigration

class DataBaseMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var updatedVersion = oldVersion

        if (updatedVersion == 0L) {
            schema.get("Card").addField("isTrash", Boolean::class.java)
            schema.get("Holder").addField("isTrash", Boolean::class.java)
            schema.get("Debt").addField("isTrash", Boolean::class.java)
            updatedVersion++
        }

        if (updatedVersion == 1L) {
            schema.get("Holder")
                    .removePrimaryKey()
                    .removeField("id")
                    .addPrimaryKey("name")
            schema.get("Card")
                    .removePrimaryKey()
                    .removeField("id")
                    .addPrimaryKey("number")
            updatedVersion++
        }

        if (updatedVersion == 2L) {
            schema.get("Holder")
                    .removeField("cardsCount")
                    .removeField("debtCount")
                    .addRealmListField("cards", schema.get("Card"))
                    .addRealmListField("debts", schema.get("Debt"))
            updatedVersion++
        }

        if (updatedVersion == 3L) {
            updatedVersion++
        }
    }
}