package com.ziss.storyapp.presentation.ui.activities.storyDetail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.ziss.storyapp.R
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.databinding.ActivityStoryDetailBinding
import com.ziss.storyapp.utils.loadImage

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbarLayout.toolbar)
        supportActionBar?.title = getString(R.string.story_detail_title)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playAnimation()
        fetchStory()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun playAnimation() {
        val viewObjects = listOf(
            binding.tvItemName,
            binding.tvDetailDescription,
        )

        val objectAnimators = viewObjects.map {
            ObjectAnimator.ofFloat(it, View.ALPHA, 1f).apply {
                duration = 150
                startDelay = 150
            }
        }

        AnimatorSet().apply {
            playSequentially(objectAnimators)
            start()
        }
    }

    private fun setData(story: StoryModel?) {
        binding.apply {
            tvItemName.text = story?.name
            tvDetailDescription.text = story?.description
            ivItemPhoto.loadImage(story?.photoUrl)
        }
    }

    private fun fetchStory() {
        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, StoryModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORY)
        }
        setData(story)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
        fun start(
            context: Context,
            story: StoryModel,
            optionsCompat: ActivityOptionsCompat
        ) {
            Intent(context, StoryDetailActivity::class.java).apply {
                this.putExtra(EXTRA_STORY, story)
                context.startActivity(this, optionsCompat.toBundle())
            }
        }
    }
}