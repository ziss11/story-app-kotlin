package com.ziss.storyapp.presentation.ui.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ziss.storyapp.R
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.databinding.ActivityStoryDetailBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.StoryViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState
import com.ziss.storyapp.utils.loadImage

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var factory: ViewModelFactory

    private val authViewModel: AuthViewModel by viewModels { factory }
    private val storyViewModel: StoryViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbarLayout.toolbar)
        supportActionBar?.title = getString(R.string.story_detail_title)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        factory = ViewModelFactory.getInstance(dataStore)

        playAnimation()
        fetchToken()
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
            binding.ivAvatar,
            binding.tvItemName,
            binding.ivItemPhoto,
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

    private fun setDescription(text: String): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(text)
        spannable.apply {
            setSpan(
                StyleSpan(Typeface.BOLD), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }

    private fun setData(story: StoryModel) {
        binding.apply {
            tvItemName.text = story.name
            tvDetailDescription.text = setDescription("${story.name} ${story.description}")
            ivItemPhoto.loadImage(story.photoUrl)
        }
    }

    private fun fetchToken() {
        authViewModel.getToken().observe(this) {
            fetchStory(it)
        }
    }

    private fun fetchStory(token: String) {
        val id = intent.getStringExtra(EXTRA_ID) as String
        storyViewModel.getStoryById(token, id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading()
                    showMessage(false)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    showMessage(false)
                    setData(result.data.story)
                }

                is ResultState.Failed -> {
                    showLoading(false)
                    showMessage()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean = true) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rlStory.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rlStory.visibility = View.VISIBLE
        }
    }

    private fun showMessage(isShowMessage: Boolean = true) {
        if (isShowMessage) {
            binding.apply {
                tvMessage.visibility = View.VISIBLE
                rlStory.visibility = View.GONE
            }
        } else {
            binding.apply {
                tvMessage.visibility = View.GONE
                rlStory.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        fun start(context: Context, id: String) {
            Intent(context, StoryDetailActivity::class.java).apply {
                this.putExtra(EXTRA_ID, id)
                context.startActivity(this)
            }
        }
    }
}