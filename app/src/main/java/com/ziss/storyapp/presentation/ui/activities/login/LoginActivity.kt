package com.ziss.storyapp.presentation.ui.activities.login

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
import com.ziss.storyapp.databinding.ActivityLoginBinding
import com.ziss.storyapp.presentation.ViewModelFactory
import com.ziss.storyapp.presentation.ui.activities.register.RegisterActivity
import com.ziss.storyapp.presentation.viewmodels.LoginViewModel
import com.ziss.storyapp.utils.ResultState
import java.util.Locale

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { factory }

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
        factory = ViewModelFactory.getInstance(this)

        checkAuth()

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

                loginViewModel.login(email, password).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> showLoading()

                        is ResultState.Success -> {
                            showLoading(false)
                            loginViewModel.setToken(result.data.loginResult.token)
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

    private fun checkAuth() {
        loginViewModel.getToken().observe(this) { token ->
            if (!token.isNullOrEmpty()) {
                MainActivity.start(this)
                finish()
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
                if (Locale.getDefault().language == "en") 20 else 17,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                StyleSpan(Typeface.BOLD),
                if (Locale.getDefault().language == "en") 20 else 17,
                text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
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