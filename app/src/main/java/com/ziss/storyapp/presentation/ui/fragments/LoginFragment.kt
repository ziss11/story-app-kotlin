package com.ziss.storyapp.presentation.ui.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ziss.storyapp.R
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.databinding.FragmentLoginBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState

class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory

    //
    private val authViewModel: AuthViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity().dataStore)

        setSpanText()
        playAnimation()
        editTextListener()

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_not_register -> v.findNavController()
                .navigate(R.id.action_loginFragment_to_registerFragment)

            R.id.btn_login -> {
                Log.d("TEST", "clicked")
                hideKeyboard(v)

                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()

                authViewModel.login(email, password).observe(requireActivity()) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> showLoading()

                            is ResultState.Success -> {
                                showLoading(false)
                                authViewModel.setToken(result.data.loginResult.token)
                            }

                            is ResultState.Failed -> {
                                showLoading(false)
                                val message =
                                    requireActivity().getString(R.string.email_pass_incorrect)
                                Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                                    .setBackgroundTint(requireActivity().getColor(R.color.red))
                                    .show()
                            }
                        }
                    }
                }

            }
        }
    }

    private fun showLoading(loading: Boolean = true) {
        binding.btnLogin.isClickable = !loading

        if (loading) {
            binding.btnLogin.visibility = View.INVISIBLE
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.progressIndicator.visibility = View.GONE
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService<InputMethodManager>()
        inputMethodManager?.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    private fun editTextListener() {
        binding.edLoginEmail.addTextChangedListener { setMyButtonEnable() }
        binding.edLoginPassword.addTextChangedListener { setMyButtonEnable() }
    }

    private fun setMyButtonEnable() {
        val isEnabled = !binding.edLoginEmail.text.isNullOrEmpty()
                && !binding.edLoginPassword.text.isNullOrEmpty()
                && binding.edLoginEmail.error == null
                && binding.edLoginPassword.error == null

        binding.btnLogin.isEnabled = isEnabled
    }

    private fun setSpanText() {
        val text = getString(R.string.not_registered)
        val spannable = SpannableStringBuilder(text)
        spannable.apply {
            setSpan(
                ForegroundColorSpan(requireActivity().getColor(R.color.orange)),
                20,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                StyleSpan(Typeface.BOLD),
                20,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvNotRegister.text = spannable
        binding.tvNotRegister.setOnClickListener(this)
    }

    private fun playAnimation() {
        val viewObjects = listOf<View>(
            binding.loginTitle,
            binding.loginSubtitle,
            binding.edLoginEmail,
            binding.edLoginPassword,
            binding.btnLogin,
            binding.tvNotRegister
        )

        val objectAnimators = viewObjects.map {
            ObjectAnimator.ofFloat(it, View.ALPHA, 1f).apply {
                duration = 100
                startDelay = 50
            }
        }

        AnimatorSet().apply {
            playSequentially(objectAnimators)
            start()
        }
    }
}