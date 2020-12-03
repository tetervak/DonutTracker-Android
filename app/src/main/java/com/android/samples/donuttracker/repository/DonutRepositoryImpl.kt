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

    override suspend fun get(id: String): Donut {
        return donutDao.get(id.toLong()).asDonut()
    }

    override suspend fun insert(donut: Donut): Long {
        return donutDao.insert(donut.asEntity())
    }

    override suspend fun delete(donut: Donut) {
        if (donut.id != null) {
            donutDao.delete(donut.id.toLong())
        }
    }

    override suspend fun update(donut: Donut) {
        donutDao.update(donut.asEntity())
    }
}

fun DonutEntity.asDonut(): Donut {
    return Donut(id.toString(), name, description, rating, lowFat)
}

fun Donut.asEntity(): DonutEntity {
    return DonutEntity(id?.toLong() ?: 0L, name, description, rating, lowFat)
}