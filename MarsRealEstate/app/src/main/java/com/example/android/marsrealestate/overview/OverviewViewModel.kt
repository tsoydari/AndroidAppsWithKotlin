/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiStatus
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.await

class OverviewViewModel : ViewModel() {

    // The MutableLiveData String that stores the status of the most recent request
    val status = MutableLiveData<MarsApiStatus?>(MarsApiStatus.LOADING)
    val properties = MutableLiveData<List<MarsProperty>>()
    val navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    init {
        getMarsRealEstateProperties()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {
                status.postValue(MarsApiStatus.LOADING)
                var listResult = getPropertiesDeferred.await()
                status.postValue(MarsApiStatus.DONE)
                if (listResult.size > 0) {
                    properties.postValue(listResult)
                }
            } catch (e: Exception) {
                status.postValue(MarsApiStatus.ERROR)
            }
        }
    }

    fun displayPropertyDetails(marsProperty: MarsProperty) {
        navigateToSelectedProperty.postValue(marsProperty)
    }

    fun displayPropertyDetailsComplete() {
        navigateToSelectedProperty.postValue(null)
    }
}
