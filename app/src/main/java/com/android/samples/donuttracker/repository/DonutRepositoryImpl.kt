package com.android.samples.donuttracker.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.android.samples.donuttracker.database.DonutDao
import com.android.samples.donuttracker.database.DonutEntity
import com.android.samples.donuttracker.domain.Donut
import javax.inject.Inject

class DonutRepositoryImpl @Inject constructor(private val donutDao: DonutDao)
    : DonutRepository {

    override fun getAll(): LiveData<List<Donut>> {
        return Transformations.map(donutDao.getAll()) { list -> list.map { it.asDonut() }}
    }

    override suspend fun get(id: Long): Donut {
        return donutDao.get(id).asDonut()
    }

    override suspend fun insert(donut: Donut): Long {
        return donutDao.insert(donut.asEntity())
    }

    override suspend fun delete(donut: Donut) {
        donutDao.delete(donut.id)
    }

    override suspend fun update(donut: Donut) {
        donutDao.update(donut.asEntity())
    }
}

fun DonutEntity.asDonut(): Donut {
    return Donut(id, name, description, rating, lowFat)
}

fun Donut.asEntity(): DonutEntity {
    return DonutEntity(id, name, description, rating, lowFat)
}