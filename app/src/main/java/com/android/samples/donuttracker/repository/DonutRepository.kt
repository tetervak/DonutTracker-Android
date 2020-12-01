package com.android.samples.donuttracker.repository

import androidx.lifecycle.LiveData
import com.android.samples.donuttracker.domain.Donut

interface DonutRepository {
    fun getAll(): LiveData<List<Donut>>
    fun get(id: String): LiveData<Donut>
    suspend fun insert(donut: Donut): Long
    suspend fun delete(donut: Donut)
    suspend fun update(donut: Donut)
}