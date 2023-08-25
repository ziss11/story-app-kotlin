package com.ziss.storyapp.presentation.ui

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ziss.storyapp.R
import com.ziss.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpanText()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_not_register -> RegisterActivity.start(this)
        }
    }

    private fun setSpanText() {
        val text = getString(R.string.not_registered)
        val spannable = SpannableStringBuilder(text)
        spannable.apply {
            setSpan(
                ForegroundColorSpan(getColor(R.color.orange)),
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

    companion object {
        fun start(context: Context) {
            Intent(context, LoginActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}