package com.example.android.gdgfinder.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.gdgfinder.R
import com.example.android.gdgfinder.network.GdgChapterDTO
import com.example.android.gdgfinder.search.GdgListAdapter.GdgListViewHolder

class GdgListAdapter(private val clickListener: (chapterDTO: GdgChapterDTO) -> Unit): ListAdapter<GdgChapterDTO, GdgListViewHolder>(DiffCallback){
    companion object DiffCallback : DiffUtil.ItemCallback<GdgChapterDTO>() {
        override fun areItemsTheSame(oldItem: GdgChapterDTO, newItem: GdgChapterDTO): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: GdgChapterDTO, newItem: GdgChapterDTO): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs a new [ViewHolder].
     *
     * A ViewHolder holds a view for the [RecyclerView] as well as providing additional information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GdgListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return GdgListViewHolder(view)

    }

    /**
     * Part of the RecyclerView adapter, called when RecyclerView needs to show an item.
     *
     * The ViewHolder passed may be recycled, so make sure that this sets any properties that
     * may have been set previously.
     */
    override fun onBindViewHolder(holder: GdgListViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    class GdgListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val layout: ConstraintLayout = itemView.findViewById(R.id.layoutItem)
        private val name: TextView = itemView.findViewById(R.id.tvChapterName)

        fun bind(clickListener: (chapterDTO: GdgChapterDTO) -> Unit, gdgChapterDTO: GdgChapterDTO) {
            layout.setOnClickListener{clickListener(gdgChapterDTO)}
            name.text = gdgChapterDTO.name
        }

    }
}
