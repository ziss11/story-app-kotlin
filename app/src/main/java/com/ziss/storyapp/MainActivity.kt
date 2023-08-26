package com.ziss.storyapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.databinding.ActivityMainBinding
import com.ziss.storyapp.presentation.ui.activities.StoryDetailActivity
import com.ziss.storyapp.presentation.ui.adapters.StoryAdapter
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.StoryViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var storyAdapter: StoryAdapter

    private val authViewModel: AuthViewModel by viewModels { factory }
    private val storyViewModel: StoryViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appbarLayout.toolbar)
        supportActionBar?.title = getString(R.string.story_title)

        factory = ViewModelFactory.getInstance(dataStore)
        setListAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_action -> {
                logoutDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setListAdapter() {
        val layout = LinearLayoutManager(this)

        storyAdapter = StoryAdapter()
        storyAdapter.setOnClickItemCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryModel) {
                StoryDetailActivity.start(this@MainActivity, story.id)
            }
        })

        binding.apply {
            rvStory.adapter = storyAdapter
            rvStory.layoutManager = layout
        }
        fetchToken()
    }

    private fun fetchToken() {
        authViewModel.getToken().observe(this) {
            fetchStories(it)
        }
    }

    private fun fetchStories(token: String) {
        storyViewModel.getStories(token).observe(this) { result ->
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
            binding.progressBar.visibility = View.VISIBLE
        } else {
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
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.logout_alert))
                .setPositiveButton(R.string.ok) { _, _ -> authViewModel.setToken("") }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            create()
            show()
        }
    }
}