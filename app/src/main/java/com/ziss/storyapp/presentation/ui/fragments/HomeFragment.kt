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
import com.ziss.storyapp.R
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.databinding.FragmentHomeBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory

class HomeFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory

    private val authViewModel: AuthViewModel by viewModels { factory }

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

        binding.appbarLayout.toolbar.title = requireActivity().getString(R.string.story_title)
        binding.appbarLayout.toolbar.inflateMenu(R.menu.home_menu)
        binding.appbarLayout.toolbar.setOnMenuItemClickListener(this)
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