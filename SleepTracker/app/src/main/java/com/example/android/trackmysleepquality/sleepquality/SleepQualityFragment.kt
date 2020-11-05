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

package com.example.android.trackmysleepquality.sleepquality

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import kotlinx.android.synthetic.main.fragment_sleep_quality.*

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment(R.layout.fragment_sleep_quality) {

    private val application by lazy{ requireNotNull(activity).application }

    private val dataSource by lazy{ SleepDatabase.getInstance(application).sleepDatabaseDao }

    private val arguments by lazy { SleepQualityFragmentArgs.fromBundle(requireArguments()) }

    private val sleepQualityViewModel: SleepQualityViewModel
            by viewModels {
                SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(activity).application

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        sleepQualityViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                requireView().findNavController().navigate(
                        SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                sleepQualityViewModel.doneNavigating()
            }
        })
    }

    private fun initListeners() {
        quality_zero_image.setOnClickListener{sleepQualityViewModel.onSetSleepQuality(0)}
        quality_one_image.setOnClickListener{sleepQualityViewModel.onSetSleepQuality(1)}
        quality_two_image.setOnClickListener{sleepQualityViewModel.onSetSleepQuality(2)}
        quality_three_image.setOnClickListener{sleepQualityViewModel.onSetSleepQuality(3)}
        quality_four_image.setOnClickListener{sleepQualityViewModel.onSetSleepQuality(4)}
        quality_five_image.setOnClickListener{sleepQualityViewModel.onSetSleepQuality(5)}

    }

}
