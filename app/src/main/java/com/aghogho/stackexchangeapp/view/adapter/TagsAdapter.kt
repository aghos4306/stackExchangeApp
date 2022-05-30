package com.aghogho.stackexchangeapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aghogho.stackexchangeapp.databinding.TagItemBinding

class TagsAdapter(): ListAdapter<String, TagsAdapter.ItemViewHolder>(DiffUtilCallback()) {

    inner class ItemViewHolder(
        private val binding: TagItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(tag: String) = binding.apply {
            tagItemText.text = tag
        }
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val tag = getItem(position)
        holder.bind(tag)
    }

}


