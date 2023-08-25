package com.ziss.storyapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.ziss.storyapp.data.models.StoryModel

class StoryDiffCallback(
    private val oldListItem: List<StoryModel>,
    private val newListItem: List<StoryModel>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldListItem.size

    override fun getNewListSize() = newListItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldListItem[oldItemPosition].id == newListItem[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldListItem[oldItemPosition]
        val newItem = newListItem[newItemPosition]
        return oldItem.id == newItem.id && oldItem.photoUrl == newItem.photoUrl
    }
}