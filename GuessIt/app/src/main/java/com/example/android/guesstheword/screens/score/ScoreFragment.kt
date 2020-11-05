/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import kotlinx.android.synthetic.main.score_fragment.*

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment(R.layout.score_fragment) {

    private val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
    private val viewModel : ScoreViewModel by viewModels {ScoreViewModelFactory(scoreFragmentArgs.score)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        play_again_button.setOnClickListener { viewModel.onPlayAgain() }
    }

    private fun initObservers() {
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            score_text.text = newScore.toString()
        })

        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })
    }
}
