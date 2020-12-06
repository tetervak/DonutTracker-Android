package com.android.samples.donuttracker.repository

import androidx.lifecycle.LiveData
import com.android.samples.donuttracker.domain.Donut
import kotlinx.coroutines.flow.Flow

interface DonutRepository {
//    fun getAll(): LiveData<List<Donut>>
    fun getAllFlow(): Flow<List<Donut>>
    suspend fun get(id: String): Donut?
    suspend fun insert(donut: Donut): String
    suspend fun delete(donut: Donut)
    suspend fun update(donut: Donut)
}