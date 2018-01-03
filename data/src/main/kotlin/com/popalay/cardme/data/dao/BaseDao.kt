/*
 * Created by popalay on 16.12.17 20:10
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 16.12.17 20:10
 */

package com.popalay.cardme.data.dao

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

interface BaseDao<in T> {

    @Insert fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(data: T)

    @Delete fun delete(data: T)
}