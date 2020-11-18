package com.android.samples.donuttracker.database

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideDonutDao(application: Application): DonutDao {
        return DonutDatabase.getInstance(application).donutDao()
    }
}