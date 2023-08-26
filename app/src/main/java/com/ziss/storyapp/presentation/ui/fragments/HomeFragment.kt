package com.ziss.storyapp.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziss.storyapp.R
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.databinding.FragmentHomeBinding
import com.ziss.storyapp.presentation.ui.adapters.StoryAdapter
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.StoryViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState

class HomeFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private lateinit var storyAdapter: StoryAdapter

    private val authViewModel: AuthViewModel by viewModels { factory }
    private val storyViewModel: StoryViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity().dataStore)

        setToolbar()
        setListAdapter()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.logout_action -> {
                logoutDialog()
                true
            }

            else -> false
        }
    }

    private fun setToolbar() {
        binding.appbarLayout.toolbar.title = requireActivity().getString(R.string.story_title)
        binding.appbarLayout.toolbar.inflateMenu(R.menu.home_menu)
        binding.appbarLayout.toolbar.setOnMenuItemClickListener(this)
    }

    private fun setListAdapter() {
        val layout = LinearLayoutManager(requireActivity())

        storyAdapter = StoryAdapter()
        storyAdapter.setOnClickItemCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryModel) {

            }
        })

        binding.apply {
            rvStory.adapter = storyAdapter
            rvStory.layoutManager = layout
        }
        fetchToken()
    }

    private fun fetchToken() {
        authViewModel.getToken().observe(requireActivity()) {
            fetchStory(it)
        }
    }

    private fun fetchStory(token: String) {
        storyViewModel.getStories(token).observe(requireActivity()) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading()
                    showMessage(false)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    val stories = result.data.stories

                    if (stories.isNotEmpty()) {
                        showMessage(false)
                        storyAdapter.setStories(stories)
                    } else {
                        showMessage()
                    }
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
            binding.tvMessage.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.tvMessage.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showMessage(isShowMessage: Boolean = true) {
        if (isShowMessage) {
            binding.apply {
                tvMessage.visibility = View.VISIBLE
                rvStory.visibility = View.GONE
            }
        } else {
            binding.apply {
                tvMessage.visibility = View.GONE
                rvStory.visibility = View.VISIBLE
            }
        }
    }

    private fun logoutDialog() {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(getString(R.string.logout_alert))
                .setPositiveButton(R.string.ok,
                    { dialog, id ->
                        authViewModel.setToken("")
                    })
                .setNegativeButton(R.string.cancel,
                    { dialog, id -> dialog.cancel() })
            create()
            show()
        }
    }
}