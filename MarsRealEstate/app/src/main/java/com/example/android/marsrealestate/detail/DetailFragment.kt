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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.marsrealestate.R
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(R.layout.fragment_detail) {
    val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(DetailFragmentArgs.fromBundle(requireArguments()).selectedProperty,
                requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val marsProperty = detailViewModel.selectedProperty.value

        tvPrice.text = detailViewModel.displayPropertyPrice ?: "Not found price"
        tvProperty.text = detailViewModel.displayPropertyType ?: "Not found type"

        marsProperty?.run {
            imgSrcUrl?.run {
                val imgUri = toUri().buildUpon().scheme("https").build()
                Glide.with(ivMainPhoto.context)
                        .load(imgUri)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.ic_broken_image))
                        .into(ivMainPhoto)
            }
        }
    }
}


