package com.android.samples.donuttracker.ui.entry

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.android.samples.donuttracker.domain.Donut
import com.android.samples.donuttracker.repository.DonutRepository

class DonutEntryViewModel @ViewModelInject constructor(
        private val repository: DonutRepository
) : ViewModel() {

    private val donutId = MutableLiveData<String?>()

    fun loadData(id: String?) {
        donutId.value = id
    }

    val donut: LiveData<Donut> =
            donutId.switchMap { id ->
                liveData {
                    if (id == null) {
                        emit(Donut(null, "", "", 3.0F))
                    } else {
                        emit(repository.get(id))
                    }
                }
            }
}
