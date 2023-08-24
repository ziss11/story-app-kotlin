package com.ziss.storyapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ziss.storyapp.R
import com.ziss.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}