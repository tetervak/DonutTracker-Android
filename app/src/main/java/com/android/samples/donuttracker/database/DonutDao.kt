/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.samples.donuttracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * The Data Access Object used to retrieve and store data from/to the underlying database.
 * This API is not used directly; instead, callers should use the Repository which calls into
 * this DAO.
 */
@Dao
interface DonutDao {
    @Query("SELECT * FROM donuts")
    fun getAll(): LiveData<List<DonutEntity>>

    @Query("SELECT * FROM donuts WHERE id = :id")
    suspend fun get(id: Long): DonutEntity

    @Insert
    suspend fun insert(donut: DonutEntity): Long

    @Delete
    suspend fun delete(donut: DonutEntity)

    @Query("DELETE FROM donuts WHERE id=:id")
    suspend fun delete(id: Long)

    @Update
    suspend fun update(donut: DonutEntity)
}
