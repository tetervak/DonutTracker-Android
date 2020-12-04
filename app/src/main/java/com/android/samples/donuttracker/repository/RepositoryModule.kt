package com.android.samples.donuttracker.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindDonutRepository(repository: DonutRepositoryFirestore): DonutRepository
}