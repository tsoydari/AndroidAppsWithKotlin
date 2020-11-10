package com.example.android.trackmysleepquality.sleepdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SleepDetailViewModelFactory(
        private val sleepNightKeyDetail: Long,
        private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)) {
            return SleepDetailViewModel(sleepNightKeyDetail, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}