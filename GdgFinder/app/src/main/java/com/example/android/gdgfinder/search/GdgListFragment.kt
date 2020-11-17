package com.example.android.gdgfinder.search

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.example.android.gdgfinder.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_gdg_list.*

private const val LOCATION_PERMISSION_REQUEST = 1

private const val LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"

class GdgListFragment : Fragment(R.layout.fragment_gdg_list) {


    private val gdgListViewModel: GdgListViewModel by viewModels()
    private val gdgListAdapter: GdgListAdapter by lazy { initAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        rvGdgChapterList.adapter = gdgListAdapter

        initObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLastLocationOrStartLocationUpdates()
    }

    private fun initAdapter() = GdgListAdapter(GdgClickListener { chapter ->
        val destination = Uri.parse(chapter.website)
        startActivity(Intent(Intent.ACTION_VIEW, destination))
    })

    private fun initObservers() {
        gdgListViewModel.showNeedLocation.observe(viewLifecycleOwner, object: Observer<Boolean> {
            override fun onChanged(show: Boolean?) {
                // Snackbar is like Toast but it lets us show forever
                if (show == true) {
                    Snackbar.make(
                        clGdgList,
                        "No location. Enable location in settings (hint: test with Maps) then check app permissions!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })

        gdgListViewModel.gdgList.observe(viewLifecycleOwner, Observer { data ->
            gdgListAdapter.submitList(data) {
                // scroll the list to the top after the diffs are calculated and posted
                rvGdgChapterList.scrollToPosition(0)
            }

            tvGdgChapterList.visibility = when {
                data == null || data.isEmpty() -> View.VISIBLE
                else -> View.GONE
            }
        })

        gdgListViewModel.regionList.observe(viewLifecycleOwner, object: Observer<List<String>> {
            override fun onChanged(data: List<String>?) {
                data ?: return
                val children = data.map { regionName ->
                    val chip = LayoutInflater.from(chipRegionList.context)
                        .inflate(R.layout.region, chipRegionList, false) as Chip
                    chip.text = regionName
                    chip.tag = regionName
                    chip.setOnCheckedChangeListener { button, isChecked ->
                        gdgListViewModel.onFilterChanged(button.tag as String, isChecked)
                    }
                    chip
                }

                chipRegionList.removeAllViews()

                for (chip in children) {
                    chipRegionList.addView(chip)
                }
            }
        })
    }

    /**
     * Show the user a dialog asking for permission to use location.
     */
    private fun requestLocationPermission() {
        requestPermissions(arrayOf(LOCATION_PERMISSION), LOCATION_PERMISSION_REQUEST)
    }

    /**
     * Request the last location of this device, if known, otherwise start location updates.
     *
     * The last location is cached from the last application to request location.
     */
    private fun requestLastLocationOrStartLocationUpdates() {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
                startLocationUpdates(fusedLocationClient)
            } else {
                gdgListViewModel.onLocationUpdated(location)
            }
        }
    }

    /**
     * Start location updates, this will ask the operating system to figure out the devices location.
     */
    private fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }


        val request = LocationRequest().setPriority(LocationRequest.PRIORITY_LOW_POWER)
        val callback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val location = locationResult?.lastLocation ?: return
                gdgListViewModel.onLocationUpdated(location)
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, null)
    }

    /**
     * This will be called by Android when the user responds to the permission request.
     *
     * If granted, continue with the operation that the user gave us permission to do.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLastLocationOrStartLocationUpdates()
                }
            }
        }
    }
}


