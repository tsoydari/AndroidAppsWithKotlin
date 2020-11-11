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

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.await

class OverviewViewModel : ViewModel() {

    // The MutableLiveData String that stores the status of the most recent request
    val status = MutableLiveData<String>("Set the Mars API Response here!")
    val properties = MutableLiveData<List<MarsProperty>>()
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
//    With Default java.lang.IllegalStateException: Cannot invoke setValue on a background thread
//    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

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
                var listResult = getPropertiesDeferred.await()
                if (listResult.size > 0) {
                    Log.i("OverviewViewModel", "Size ${listResult.size.toString()}")
                    properties.postValue(listResult)
                }
                status.value = "Success: ${listResult.size} Mars properties retrieved"
            } catch (e: Exception) {
                Log.e("OverviewViewModel", e.message.toString())
                status.value = "Failure: ${e.message}"
            }
        }
    }
}
