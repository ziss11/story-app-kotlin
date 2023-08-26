package com.ziss.storyapp.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziss.storyapp.R
import com.ziss.storyapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbarLayout.toolbar)
        supportActionBar?.title = getString(R.string.new_story_title)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    companion object {
        const val UPLOAD_SUCCESS = "upload_success"
        fun start(context: Context) {
            val intent = Intent(context, AddStoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}