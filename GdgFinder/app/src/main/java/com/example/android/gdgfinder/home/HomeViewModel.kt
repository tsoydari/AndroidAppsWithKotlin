package com.example.android.gdgfinder.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class HomeViewModel : ViewModel() {
    val navigateToSearch = MutableLiveData<Boolean>()

    fun onFabClicked() {
        navigateToSearch.value = true
    }

    fun onNavigatedToSearch() {
        navigateToSearch.value = false
    }
}