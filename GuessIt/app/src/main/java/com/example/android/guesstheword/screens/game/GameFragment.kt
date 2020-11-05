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
import android.text.format.DateUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import kotlinx.android.synthetic.main.game_fragment.*

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment(R.layout.game_fragment) {

//    private val viewModel : GameViewModel by lazy { ViewModelProviders.of(this).get(GameViewModel::class.java) }
    private val viewModel : GameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        correct_button.setOnClickListener {
            viewModel.onCorrect()
        }
        skip_button.setOnClickListener {
            viewModel.onSkip()
        }
    }

    private fun initObservers() {
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
//            score_text.text = newScore.toString()
            score_text.text = getString(R.string.score_format, newScore)
        })

        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
//            word_text.text = newWord
            word_text.text = getString(R.string.quote_format, newWord)
        })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer {
            if (it) {
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        viewModel.currentTime.observe(viewLifecycleOwner, Observer {newTime ->
            timer_text.text = DateUtils.formatElapsedTime(newTime)
        })
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }
}
