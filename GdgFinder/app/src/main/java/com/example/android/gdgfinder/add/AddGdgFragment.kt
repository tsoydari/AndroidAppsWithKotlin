package com.example.android.gdgfinder.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.gdgfinder.R
import com.google.android.material.snackbar.Snackbar

class AddGdgFragment : Fragment(R.layout.add_gdg_fragment) {

    private val addGdgViewModel: AddGdgViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initObservers()
    }

    private fun initObservers() {
        addGdgViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.application_submitted),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                addGdgViewModel.doneShowingSnackbar()
            }
        })
    }

}
