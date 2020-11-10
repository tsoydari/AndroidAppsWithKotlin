package com.example.android.trackmysleepquality.sleepdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import kotlinx.android.synthetic.main.fragment_sleep_detail.*

class SleepDetailFragment : Fragment(R.layout.fragment_sleep_detail) {

    private val sleepDetailFragmentViewModel: SleepDetailViewModel
            by viewModels {
                SleepDetailViewModelFactory(
                        SleepDetailFragmentArgs.fromBundle(requireArguments()).sleepNightKeyDetail,
                        requireActivity().application)
            }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        sleepDetailFragmentViewModel.navigateToSleepTracker.observe(viewLifecycleOwner,
        Observer {
            if (it) { // Observed state is true.
                requireView().findNavController().navigate(
                        SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment())
                sleepDetailFragmentViewModel.doneNavigating()
            }
        })
        sleepDetailFragmentViewModel.night.observe(viewLifecycleOwner, Observer {
            ivQualityDetail.setImageResource(when (it.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
            tvQualityDetail.text = context?.resources?.let {res ->
                convertNumericQualityToString(it.sleepQuality, res)
            }
                    it.sleepQuality.toString()
            tvSleepLengthDetail.text = context?.resources?.let { res ->
                convertDurationToFormatted(it.startTimeMilli,
                        it.endTimeMilli,
                        res)
            }
        })
    }

    private fun initListeners() {
        btnFromDetail.setOnClickListener { sleepDetailFragmentViewModel.onClose() }
    }
}