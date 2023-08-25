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
import com.ziss.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpanText()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_have_account -> LoginActivity.start(this)
        }
    }

    private fun setSpanText() {
        val text = getString(R.string.have_account)
        val spannable = SpannableStringBuilder(text)
        spannable.apply {
            setSpan(
                ForegroundColorSpan(getColor(R.color.orange)),
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

    companion object {
        fun start(context: Context) {
            Intent(context, RegisterActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}