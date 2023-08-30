package com.ziss.storyapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.databinding.StoryItemBinding
import com.ziss.storyapp.utils.loadImage

class StoryAdapter : PagingDataAdapter<StoryModel, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(story: StoryModel, storyImage: ImageView)
    }

    inner class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryModel) {
            binding.apply {
                tvItemName.text = story.name
                ivItemPhoto.loadImage(story.photoUrl)
            }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(story, binding.ivItemPhoto)
            }
        }
    }

    fun setOnClickItemCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) holder.bind(story)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(
                oldItem: StoryModel,
                newItem: StoryModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryModel,
                newItem: StoryModel
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}