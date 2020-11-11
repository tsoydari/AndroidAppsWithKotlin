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

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsApiStatus
import kotlinx.android.synthetic.main.fragment_overview.*

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val overviwViewModel: OverviewViewModel by viewModels()

    val adapter by lazy { PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
        overviwViewModel.displayPropertyDetails(it)
    }) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        rvGridPhotos.adapter = adapter
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        overviwViewModel.updateFilter(
                when (item.itemId) {
                    R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                    R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                    else -> MarsApiFilter.SHOW_ALL
                }
        )
        return true
    }

    private fun initObservers() {
        overviwViewModel.properties.observe(viewLifecycleOwner, Observer {
            it?.run {
                adapter.submitList(it)
            }
        })

        overviwViewModel.status.observe(viewLifecycleOwner, Observer {status ->
            when (status) {
                MarsApiStatus.LOADING -> {
                    ivStatus.visibility = View.VISIBLE
                    ivStatus.setImageResource(R.drawable.loading_animation)
                }
                MarsApiStatus.ERROR -> {
                    ivStatus.visibility = View.VISIBLE
                    ivStatus.setImageResource(R.drawable.ic_connection_error)
                }
                MarsApiStatus.DONE -> {
                    ivStatus.visibility = View.GONE
                }
                else -> {
                    ivStatus.visibility = View.GONE
                }
            }
        })

        overviwViewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                requireView().findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                overviwViewModel.displayPropertyDetailsComplete()
            }
        })
    }
}
