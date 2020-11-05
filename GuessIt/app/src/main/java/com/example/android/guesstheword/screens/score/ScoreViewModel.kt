package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {
    val score = MutableLiveData<Int>()
    val eventPlayAgain = MutableLiveData<Boolean>()

    init {
        Log.i("ScoreViewModel", "Final score is $finalScore")
        score.value = finalScore
    }

    fun onPlayAgain() {
        eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        eventPlayAgain.value = false
    }
}