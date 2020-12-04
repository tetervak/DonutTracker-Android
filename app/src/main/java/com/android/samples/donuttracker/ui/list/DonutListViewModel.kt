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
package com.android.samples.donuttracker.ui.list

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.samples.donuttracker.domain.Donut
import com.android.samples.donuttracker.repository.DonutRepository
import com.android.samples.donuttracker.ui.entry.DonutEntryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This ViewModel is used to access the underlying data and to observe changes to it.
 */
class DonutListViewModel @ViewModelInject constructor(
    private val repository: DonutRepository) : ViewModel() {

    // Users of this ViewModel will observe changes to its donuts list to know when
    // to redisplay those changes
    val donuts: LiveData<List<Donut>> = repository.getAll()

    fun delete(donut: Donut) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(donut)
    }

    fun saveData(
        id: String?,
        name: String,
        description: String,
        rating: Int
    ) {
        val donut = Donut(id, name, description, rating)
        viewModelScope.launch(Dispatchers.IO) {
            if (id == null) {
                repository.insert(donut)
            } else {
                repository.update(donut)
            }
        }
    }
}
