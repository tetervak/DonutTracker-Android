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
package com.android.samples.donuttracker.ui.entry

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.android.samples.donuttracker.domain.Donut
import com.android.samples.donuttracker.repository.DonutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DonutEntryViewModel @ViewModelInject constructor(
        private val repository: DonutRepository
) : ViewModel() {

    private val donutId = MutableLiveData<String?>()

    fun loadData(id: String?){
        donutId.value = id
    }

    val donut: LiveData<Donut> =
            donutId.switchMap {
                if (it === null) {
                    MutableLiveData(Donut(null, "", "", 3))
                } else {
                    repository.get(it)
                }
            }

    fun saveData(
        id: String?,
        name: String,
        description: String,
        rating: Int
    ) {
        val donut = Donut(id, name, description, rating)

        viewModelScope.launch(Dispatchers.IO){
            if( id == null){
                insert(donut)
            }else{
                update(donut)
            }
        }
    }

    private suspend fun insert(donut: Donut): Long {
        return repository.insert(donut)
    }

    private fun update(donut: Donut) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(donut)
    }
}
