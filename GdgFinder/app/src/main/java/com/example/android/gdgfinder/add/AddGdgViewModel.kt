package com.example.android.gdgfinder.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddGdgViewModel : ViewModel() {

    /**
     * Request a toast by setting this value to true.
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    var showSnackbarEvent = MutableLiveData<Boolean>()


    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        showSnackbarEvent.value = false
    }

    fun onSubmitApplication() {
        showSnackbarEvent.value = true
    }
}
