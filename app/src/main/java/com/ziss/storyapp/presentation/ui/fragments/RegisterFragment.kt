package com.ziss.storyapp.presentation.ui.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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
import com.ziss.storyapp.databinding.FragmentRegisterBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState

class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory

    private val authViewModel: AuthViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity().dataStore)

        setSpanText()
        playAnimation()
        editTextListener()

        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_have_account -> {
                v.findNavController()
                    .navigate(R.id.action_registerFragment_to_loginFragment)
            }

            R.id.btn_register -> {
                hideKeyboard(v)

                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()

                authViewModel.register(name, email, password).observe(requireActivity()) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> showLoading(true)

                            is ResultState.Success -> {
                                showLoading()

                                val message =
                                    requireActivity().getString(R.string.register_success)
                                Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                                    .setBackgroundTint(requireActivity().getColor(R.color.orange))
                                    .show()
                                v.findNavController()
                                    .apply { navigate(R.id.action_registerFragment_to_loginFragment) }
                            }

                            is ResultState.Failed -> {
                                showLoading()

                                val message =
                                    requireActivity().getString(R.string.email_exists)
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

    private fun showLoading(loading: Boolean = false) {
        binding.btnRegister.isClickable = !loading

        if (loading) {
            binding.btnRegister.visibility = View.INVISIBLE
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            binding.btnRegister.visibility = View.VISIBLE
            binding.progressIndicator.visibility = View.GONE
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService<InputMethodManager>()
        inputMethodManager?.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    private fun editTextListener() {
        binding.edRegisterName.addTextChangedListener { setMyButtonEnable() }
        binding.edRegisterEmail.addTextChangedListener { setMyButtonEnable() }
        binding.edRegisterPassword.addTextChangedListener { setMyButtonEnable() }
    }

    private fun setMyButtonEnable() {
        val isEnabled = !binding.edRegisterName.text.isNullOrEmpty()
                && !binding.edRegisterEmail.text.isNullOrEmpty()
                && !binding.edRegisterPassword.text.isNullOrEmpty()
                && binding.edRegisterName.error == null
                && binding.edRegisterEmail.error == null
                && binding.edRegisterPassword.error == null

        binding.btnRegister.isEnabled = isEnabled
    }

    private fun setSpanText() {
        val text = getString(R.string.have_account)
        val spannable = SpannableStringBuilder(text)
        spannable.apply {
            setSpan(
                ForegroundColorSpan(requireActivity().getColor(R.color.orange)),
                25,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                StyleSpan(Typeface.BOLD),
                25,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvHaveAccount.text = spannable
        binding.tvHaveAccount.setOnClickListener(this)
    }

    private fun playAnimation() {
        val viewObjects = listOf<View>(
            binding.registerTitle,
            binding.registerSubtitle,
            binding.edRegisterName,
            binding.edRegisterEmail,
            binding.edRegisterPassword,
            binding.btnRegister,
            binding.tvHaveAccount
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