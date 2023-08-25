package com.ziss.storyapp.presentation.ui

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
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ziss.storyapp.R
import com.ziss.storyapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpanText()
        playAnimation()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_have_account -> {
                Log.d("TEST", "TEST")
                v.findNavController()
                    .navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
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