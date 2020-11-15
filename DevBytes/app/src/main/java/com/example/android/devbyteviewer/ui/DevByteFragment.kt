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

package com.example.android.devbyteviewer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.domain.launchUri
import kotlinx.android.synthetic.main.fragment_dev_byte.*

/**
 * Show a list of DevBytes on screen.
 */
class DevByteFragment : Fragment(R.layout.fragment_dev_byte) {

    private val devByteViewModel: DevByteViewModel by viewModels()

    private val devByteAdapter by lazy { initAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFragment.adapter = devByteAdapter
        initObservers()
    }

    private fun initAdapter() = DevByteAdapter(VideoClick {
        // When a video is clicked this block or lambda will be called by DevByteAdapter

        // context is not around, we can safely discard this click since the Fragment is no
        // longer on the screen
        val packageManager = context?.packageManager ?: return@VideoClick

        // Try to generate a direct intent to the YouTube app
        var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
        if (intent.resolveActivity(packageManager) == null) {
            // YouTube app isn't found, use the web url
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
        }

        startActivity(intent)
    })

    private fun initObservers() {
        devByteViewModel.playlist.observe(viewLifecycleOwner, Observer<List<Video>> { videos ->

            videos?.run {
                devByteAdapter.videos = MutableLiveData(videos)
                rvFragment.visibility = View.GONE
            } ?:
            if (videos == null) {
                rvFragment.visibility = View.VISIBLE
            }
        })
    }
}
