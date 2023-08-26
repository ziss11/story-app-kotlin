package com.ziss.storyapp.presentation.ui.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ziss.storyapp.MainActivity
import com.ziss.storyapp.R
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.databinding.ActivityLoginBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory

    private val authViewModel: AuthViewModel by viewModels { factory }

    private val launcherRegister =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val message = it.data?.getStringExtra(RegisterActivity.REGISTER_SUCCESS) as String
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setBackgroundTint(this.getColor(R.color.orange)).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(dataStore)

        setSpanText()
        playAnimation()
        editTextListener()

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_not_register -> RegisterActivity.start(this, launcherRegister)

            R.id.btn_login -> {
                hideKeyboard(v)

                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()

                authViewModel.login(email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> showLoading()

                            is ResultState.Success -> {
                                showLoading(false)
                                authViewModel.setToken(result.data.loginResult.token)
                                MainActivity.start(this)
                                finish()
                            }

                            is ResultState.Failed -> {
                                showLoading(false)
                                val message = getString(R.string.email_pass_incorrect)
                                Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                                    .setBackgroundTint(getColor(R.color.red)).show()
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
        val inputMethodManager = getSystemService<InputMethodManager>()
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
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
                ForegroundColorSpan(getColor(R.color.orange)),
                20,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                StyleSpan(Typeface.BOLD), 20, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
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

    companion object {
        fun start(context: Context) {
            Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }
    }
}