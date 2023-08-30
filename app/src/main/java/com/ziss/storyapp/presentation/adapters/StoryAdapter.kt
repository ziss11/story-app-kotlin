package com.ziss.storyapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.databinding.StoryItemBinding
import com.ziss.storyapp.utils.StoryDiffCallback
import com.ziss.storyapp.utils.loadImage

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val stories = ArrayList<StoryModel>()

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

    fun setStories(stories: List<StoryModel>) {
        val diffCallback = StoryDiffCallback(this.stories, stories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.stories.clear()
        this.stories.addAll(stories)

        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnClickItemCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount() = stories.size
}