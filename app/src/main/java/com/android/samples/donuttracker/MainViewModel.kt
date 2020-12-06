package com.android.samples.donuttracker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.samples.donuttracker.domain.Donut
import com.android.samples.donuttracker.repository.DonutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: DonutRepository
) : ViewModel() {

    fun delete(donut: Donut) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(donut)
        }

    fun save(donut: Donut) =
        viewModelScope.launch(Dispatchers.IO) {
            if (donut.id == null) {
                repository.insert(donut)
            } else {
                repository.update(donut)
            }
        }
}