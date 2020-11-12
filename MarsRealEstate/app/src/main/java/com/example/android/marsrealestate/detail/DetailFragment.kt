/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.marsrealestate.R
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(R.layout.fragment_detail) {
    val marsProperty by lazy {
        DetailFragmentArgs.fromBundle(requireArguments()).selectedProperty
    }

    val detailViewModel: DetailViewModel by viewModels {DetailViewModelFactory(
                marsProperty,
                requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImage()
        initObservers()
    }

    fun setImage() {
        val imgUri = marsProperty.imgSrcUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(ivMainPhoto.context)
                .load(imgUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image))
                .into(ivMainPhoto)
    }

    private fun initObservers() {
        detailViewModel.displayPropertyPrice.observe(viewLifecycleOwner, Observer { price ->
            tvPrice.text = price
        })

        detailViewModel.displayPropertyType.observe(viewLifecycleOwner, Observer { type ->
            tvProperty.text = type
        })
    }
}


