package com.ziss.storyapp.presentation.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ziss.storyapp.R
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.databinding.FragmentStoryDetailBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.StoryViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState
import com.ziss.storyapp.utils.loadImage

class StoryDetailFragment : Fragment() {
    private var _binding: FragmentStoryDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory

    private val authViewModel: AuthViewModel by viewModels { factory }
    private val storyViewModel: StoryViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryDetailBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity().dataStore)

        setupToolbar()
        fetchToken()
    }

    private fun setupToolbar() {
        val activity = getActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.appbarLayout.toolbar)
        activity.supportActionBar?.title = getString(R.string.detail_story_title)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        authViewModel.getToken().observe(requireActivity()) {
            fetchStory(it)
        }
    }

    private fun fetchStory(token: String) {
        val id = arguments?.getString(EXTRA_ID) as String
        storyViewModel.getStoryById(token, id).observe(requireActivity()) { result ->
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
    }
}