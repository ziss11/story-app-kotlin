package com.ziss.storyapp.presentation.ui.fragments.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziss.storyapp.R
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.databinding.FragmentHomeBinding
import com.ziss.storyapp.presentation.ViewModelFactory
import com.ziss.storyapp.presentation.adapters.StoryAdapter
import com.ziss.storyapp.presentation.ui.activities.addStory.AddStoryActivity
import com.ziss.storyapp.presentation.ui.activities.login.LoginActivity
import com.ziss.storyapp.presentation.viewmodels.LoginViewModel

class HomeFragment : Fragment(), MenuProvider {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private lateinit var storyAdapter: StoryAdapter

    private val loginViewModel: LoginViewModel by viewModels { factory }
    private val homeViewModel: HomeViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(
            this,
            viewLifecycleOwner,
            androidx.lifecycle.Lifecycle.State.RESUMED
        )

        factory = ViewModelFactory.getInstance(requireActivity())

//        setToolbar()
        setListAdapter()

        binding.fabAddStory.setOnClickListener {
            AddStoryActivity.start(requireActivity())
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_logout -> {
                logoutDialog()
                true
            }

            R.id.action_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            else -> false
        }
    }

//    private fun setToolbar() {
//        val activity = activity as AppCompatActivity
//        activity.setSupportActionBar(binding.appbarLayout.toolbar)
//        activity.supportActionBar?.title = getString(R.string.story_title)
//    }

    private fun setListAdapter() {
        val layout = LinearLayoutManager(requireActivity())

        storyAdapter = StoryAdapter()
        storyAdapter.setOnClickItemCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryModel, storyImage: ImageView) {
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(), storyImage, "item_photo"
                )
//                StoryDetailActivity.start(requireActivity(), story, optionsCompat)
            }
        })

        binding.apply {
            rvStory.adapter = storyAdapter
            rvStory.layoutManager = layout
        }

        fetchToken()
    }

    private fun fetchToken() {
        loginViewModel.getToken().observe(requireActivity()) { token -> fetchStories(token) }
    }

    private fun fetchStories(token: String) {
        homeViewModel.getStories(token).observe(requireActivity()) { result ->
            storyAdapter.submitData(lifecycle, result)
        }
    }

    private fun showLoading(isLoading: Boolean = true) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
            setMessage(getString(R.string.logout_alert)).setPositiveButton(R.string.ok) { _, _ ->
                LoginActivity.start(requireActivity())
                requireActivity().finish()
                loginViewModel.setToken("")
            }.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }
}