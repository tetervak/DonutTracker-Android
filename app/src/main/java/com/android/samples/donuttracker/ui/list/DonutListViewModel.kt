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
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.android.samples.donuttracker.domain.Donut
import com.android.samples.donuttracker.repository.DonutRepository

/**
 * This ViewModel is used to access the underlying data and to observe changes to it.
 */
class DonutListViewModel @ViewModelInject constructor(
    repository: DonutRepository
) : ViewModel() {

    private val userId = MutableLiveData<String?>()
    fun loadData(id: String) {
        userId.value = id
    }

    fun clearData() {
        userId.value = null
    }

    val donuts: LiveData<List<Donut>> =
        userId.switchMap { id ->
            if (id != null) {
                repository.getAll()
            } else {
                MutableLiveData()
            }
        }
}
