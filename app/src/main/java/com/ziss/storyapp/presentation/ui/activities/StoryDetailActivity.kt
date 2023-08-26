package com.ziss.storyapp.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        factory = ViewModelFactory.getInstance(dataStore)

        fetchToken()
    }

    private fun setDescription(text: String): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(text)
        spannable.apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }

    private fun setData(story: StoryModel) {
        binding.apply {
            name.text = story.name
            description.text = setDescription("${story.name} ${story.description}")
            storyImage.loadImage(story.photoUrl)
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
            binding.storyItem.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.storyItem.visibility = View.VISIBLE
        }
    }

    private fun showMessage(isShowMessage: Boolean = true) {
        if (isShowMessage) {
            binding.apply {
                tvMessage.visibility = View.VISIBLE
                storyItem.visibility = View.GONE
            }
        } else {
            binding.apply {
                tvMessage.visibility = View.GONE
                storyItem.visibility = View.VISIBLE
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