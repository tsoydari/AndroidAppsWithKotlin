package com.example.android.devbyteviewer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.domain.Video


class DevByteAdapter(private val callback: VideoClick) : RecyclerView.Adapter<DevByteAdapter.DevByteViewHolder>() {

    var videos = MutableLiveData<List<Video>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.devbyte_item, parent, false)
        return DevByteViewHolder(view)
    }

    override fun getItemCount() = videos.value?.size ?: 0

    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        videos.value?.get(position)?.let { holder.bind(it, callback) }
    }

    class DevByteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val description: TextView = itemView.findViewById(R.id.tvDescription)
        private val thumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
        private val overlay: View = itemView.findViewById(R.id.clickableOverlay)

        fun bind(item: Video, callback: VideoClick) {
            title.text = item.title
            description.text = item.description
            Glide.with(itemView.context)
                    .load(item.thumbnail)
                    .into(thumbnail)
            overlay.setOnClickListener{callback.onClick(item)}
        }
    }
}