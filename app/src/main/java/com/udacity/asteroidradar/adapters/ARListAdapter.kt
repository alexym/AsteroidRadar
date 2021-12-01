package com.udacity.asteroidradar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class ARListAdapter (val clickListener: ARClickListener): ListAdapter<Asteroid, ARListAdapter.ARListViewHolder>(DiffCallback){
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    class ARListViewHolder(private var binding: ListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: ARClickListener, asteroidObj: Asteroid) {
            binding.arObj = asteroidObj
            binding.clickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ARListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return ARListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ARListViewHolder {
        return ARListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ARListViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }
}

class ARClickListener(val clickListener: (asteroidObj: Asteroid) -> Unit) {
    fun onClick(asteroidObj: Asteroid) = clickListener(asteroidObj)
}
