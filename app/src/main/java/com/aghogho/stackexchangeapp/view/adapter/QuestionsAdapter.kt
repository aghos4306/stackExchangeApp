package com.aghogho.stackexchangeapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aghogho.stackexchangeapp.databinding.QuestionItemBinding
import com.aghogho.stackexchangeapp.model.Item
import com.aghogho.stackexchangeapp.utils.getTime
import com.bumptech.glide.Glide

class QuestionsAdapter(
    private val clickListener: OnClickListener, private val context: Context
): androidx.recyclerview.widget.ListAdapter<Item, QuestionsAdapter.ItemViewHolder>(DiffUtilCallBack()) {

    inner class ItemViewHolder(
        private val binding: QuestionItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(questionItem: Item) {
            binding.apply {
                questionItemText.text = questionItem.title
                Glide.with(context).load(questionItem.owner.profile_image).into(questionItemAuthor)
                questionItemAuthorText.text = questionItem.owner.display_name
                questionItemTimestampText.getTime(questionItem.last_edit_date.toLong())
                val tagAdapter = TagsAdapter()
                questionItemTagsRv.setHasFixedSize(true)
                questionItemTagsRv.adapter = tagAdapter
                tagAdapter.submitList(questionItem.tags)
                root.setOnClickListener {
                    clickListener.openQuestion(questionItem)
                }
            }
        }
    }

    class DiffUtilCallBack: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    interface OnClickListener {
        fun openQuestion(questionItem: Item) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val questionItem = getItem(position)
        holder.bind(questionItem)
    }

}