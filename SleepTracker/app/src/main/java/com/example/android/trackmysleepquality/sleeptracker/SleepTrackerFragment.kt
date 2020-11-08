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
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.android.trackmysleepquality.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sleep_tracker.*

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment(R.layout.fragment_sleep_tracker) {

    val adapter = SleepNightAdapter()
    private val sleepTrackerViewModel: SleepTrackerViewModel by viewModels {
        SleepTrackerViewModelFactory(requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
   }

    private fun initListeners() {
        start_button.setOnClickListener { sleepTrackerViewModel.onStartTracking() }
        stop_button.setOnClickListener { sleepTrackerViewModel.onStopTracking() }
        clear_button.setOnClickListener { sleepTrackerViewModel.onClear() }
    }

    private fun initObservers() {
        sleepTrackerViewModel.nightString.observe(viewLifecycleOwner, Observer { newNight ->
//            textview.text = newNight
        })

        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.run {
                adapter.data = it
            }
        })

        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            night?.run {
                requireView().findNavController().navigate(
                        SleepTrackerFragmentDirections
                                .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                sleepTrackerViewModel.doneNavigating()
            } })

        sleepTrackerViewModel.startButtonVisible.observe(viewLifecycleOwner, Observer {
            start_button.setEnabled(it)
        })

        sleepTrackerViewModel.stopButtonVisible.observe(viewLifecycleOwner, Observer {
            stop_button.setEnabled(it)
        })

        sleepTrackerViewModel.clearButtonVisible.observe(viewLifecycleOwner, Observer {
            clear_button.setEnabled(it)
        })

        sleepTrackerViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if (it) { // Observed state is true.
                Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })
    }
}
