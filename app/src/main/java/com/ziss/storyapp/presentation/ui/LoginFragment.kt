package com.ziss.storyapp.presentation.ui

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
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ziss.storyapp.R
import com.ziss.storyapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpanText()
        playAnimation()

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_not_register -> v.findNavController()
                .navigate(R.id.action_loginFragment_to_registerFragment)

            R.id.btn_login -> v.findNavController().apply {
                navigate(R.id.action_to_homeFragment)
                clearBackStack(R.id.fragment_home)
            }
        }
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