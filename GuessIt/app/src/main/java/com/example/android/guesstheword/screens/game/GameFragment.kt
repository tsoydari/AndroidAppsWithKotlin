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

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import kotlinx.android.synthetic.main.game_fragment.*

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment(R.layout.game_fragment) {

    private val viewModel : GameViewModel by lazy { ViewModelProviders.of(this).get(GameViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel

        correct_button.setOnClickListener {
            viewModel.onCorrect()
            updateScoreText()
            updateWordText()
        }
        skip_button.setOnClickListener {
            viewModel.onSkip()
            updateScoreText()
            updateWordText()
        }
        updateScoreText()
        updateWordText()
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score)
        findNavController(this).navigate(action)
    }

    /** Methods for updating the UI **/

    private fun updateWordText() {
        word_text.text = viewModel.word
    }

    private fun updateScoreText() {
        score_text.text = viewModel.score.toString()
    }
}
