package com.example.android.devbyteviewer.ui

import com.example.android.devbyteviewer.domain.Video

class VideoClick(val block: (Video) -> Unit) {
    fun onClick(video: Video) = block(video)
}