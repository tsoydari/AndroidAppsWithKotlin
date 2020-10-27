package com.example.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()
    }

    private fun setListeners() {
        val clickableViews: List<View> =
                listOf(tvBoxOne, tvBoxTwo, tvBoxThree,
                tvBoxFour, tvBoxFive, tvBox)
        for (item in clickableViews) {
            item.setOnClickListener{makeColored(it)}
        }
    }

    private fun makeColored(view: View) = when(view.id) {
        R.id.tvBoxOne -> view.setBackgroundColor(Color.DKGRAY)
        R.id.tvBoxTwo -> view.setBackgroundColor(Color.GRAY)
        R.id.tvBoxThree ->
            view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
        R.id.tvBoxFour ->
            view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        R.id.tvBoxFive ->
            view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_purple))
        else -> view.setBackgroundColor(Color.LTGRAY)
    }
}