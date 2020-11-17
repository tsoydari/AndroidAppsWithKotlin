package com.example.android.gdgfinder.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.gdgfinder.R
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(R.layout.home_fragment) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initListenres()
        initObservers()
    }

    private fun initListenres() {
        floatBtnSearch.setOnClickListener{ homeViewModel.onFabClicked() }
    }

    private fun initObservers() {
        homeViewModel.navigateToSearch.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                requireView().findNavController().navigate(R.id.action_homeFragment_to_gdgListFragment)
                homeViewModel.onNavigatedToSearch()
            }
        })
    }
}
