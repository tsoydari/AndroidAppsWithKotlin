package com.example.android.gdgfinder.search

import android.location.Location
import androidx.lifecycle.*
import com.example.android.gdgfinder.network.GdgApi
import com.example.android.gdgfinder.network.GdgChapterDTO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException


class GdgListViewModel: ViewModel() {

    private val repository = GdgChapterRepository(GdgApi.retrofitService)
    private var filter = FilterHolder()
    private var currentJob: Job? = null

    val gdgList = MutableLiveData<List<GdgChapterDTO>>()
    val regionList = MutableLiveData<List<String>>()
    val showNeedLocation = MutableLiveData<Boolean>()

    init {
        // process the initial filter
        onQueryChanged()

        viewModelScope.launch {
            delay(5_000)
            showNeedLocation.value = !repository.isFullyInitialized
        }
    }

    private fun onQueryChanged() {
        currentJob?.cancel() // if a previous query is running cancel it before starting another
        currentJob = viewModelScope.launch {
            try {
                // this will run on a thread managed by Retrofit
                gdgList.value = repository.getChaptersForFilter(filter.currentValue)
                repository.getFilters().let {
                    // only update the filters list if it's changed since the last time
                    if (it != regionList.value) {
                        regionList.value = it
                    }
                }
            } catch (e: IOException) {
                gdgList.value = listOf()
            }
        }
    }

    fun onLocationUpdated(location: Location) {
        viewModelScope.launch {
            repository.onLocationChanged(location)
            onQueryChanged()
        }
    }

    fun onFilterChanged(filter: String, isChecked: Boolean) {
        if (this.filter.update(filter, isChecked)) {
            onQueryChanged()
        }
    }

    private class FilterHolder {
        var currentValue: String? = null
            private set

        fun update(changedFilter: String, isChecked: Boolean): Boolean {
            if (isChecked) {
                currentValue = changedFilter
                return true
            } else if (currentValue == changedFilter) {
                currentValue = null
                return true
            }
            return false
        }
    }
}

