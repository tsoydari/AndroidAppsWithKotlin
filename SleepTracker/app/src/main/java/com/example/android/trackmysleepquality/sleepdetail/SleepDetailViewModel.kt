package com.example.android.trackmysleepquality.sleepdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.Dispatchers

class SleepDetailViewModel(
        private val sleepNightKeyDetail: Long = 0L,
        application: Application) : AndroidViewModel(application)  {

    private val database: SleepDatabaseDao by lazy(Dispatchers.IO) { SleepDatabase.getInstance(application).sleepDatabaseDao }

//    val night = MediatorLiveData<SleepNight>()
//
//    init {
//        night.addSource(database.getNightWithId(sleepNightKeyDetail), night::setValue)
//    }
    val night by lazy(Dispatchers.IO){database.getNightWithId(sleepNightKeyDetail)}

    val navigateToSleepTracker = MutableLiveData<Boolean>()

    fun doneNavigating() {
        navigateToSleepTracker.value = false
    }

    fun onClose() {
        navigateToSleepTracker.value = true
    }
}