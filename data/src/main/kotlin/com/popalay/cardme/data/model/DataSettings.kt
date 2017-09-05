package com.popalay.cardme.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DataSettings(
        @PrimaryKey open var id: String? = null,
        open var language: String = "",
        open var theme: String = "",
        open var isCardBackground: Boolean = false
) : RealmObject() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as DataSettings
        if (id != other.id) return false
        if (language != other.language) return false
        if (theme != other.theme) return false
        if (isCardBackground != other.isCardBackground) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + language.hashCode()
        result = 31 * result + theme.hashCode()
        result = 31 * result + isCardBackground.hashCode()
        return result
    }
}
