package com.ziss.storyapp

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import com.ziss.storyapp.databinding.ActivityMainBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory

    private val authViewModel: AuthViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(dataStore)

        val navController = findNavController(R.id.container)

        authViewModel.getToken().observe(this) { token ->
            if (!token?.trim().isNullOrEmpty()) {
                navController.navigate(R.id.action_to_homeFragment)
            } else {
                navController.navigate(R.id.action_to_loginFragment)
            }
        }
    }
}