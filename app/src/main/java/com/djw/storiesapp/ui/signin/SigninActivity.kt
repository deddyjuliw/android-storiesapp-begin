package com.djw.storiesapp.ui.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.djw.storiesapp.R
import com.djw.storiesapp.ui.UserViewModelFactory
import com.djw.storiesapp.databinding.ActivitySigninBinding
import com.djw.storiesapp.ui.main.MainActivity
import com.djw.storiesapp.data.Result
import com.djw.storiesapp.loadAnim
import com.djw.storiesapp.ui.signup.SignupActivity

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var signinViewModel: SigninViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: UserViewModelFactory = UserViewModelFactory.getInstance(this)
        signinViewModel = ViewModelProvider(this, factory).get(
            SigninViewModel::class.java
        )

        signinViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when{
                email.isEmpty() -> {
                    binding.edLoginEmail.error = resources.getString(R.string.input_message, "Email")
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = resources.getString(R.string.input_message, "Password")
                }
                else -> {
                    signinViewModel.login(email, password).observe(this) { result ->
                        if (result != null) {
                            when(result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error) {
                                        Toast.makeText(this@SigninActivity, data.message, Toast.LENGTH_LONG).show()
                                    }
                                    else {
                                        val token = data.loginResult?.token ?: ""
                                        signinViewModel.setToken(token, true)
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, resources.getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.tvRegLogin.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginPage, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmailLogin, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPasswordLogin, View.ALPHA, 1f).setDuration(500)
        val edPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvReg = ObjectAnimator.ofFloat(binding.tvTxtReg, View.ALPHA, 1f).setDuration(500)
        val tvRegLog = ObjectAnimator.ofFloat(binding.tvRegLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                tvEmail,
                edEmail,
                tvPassword,
                edPassword,
                btnLogin,
                tvReg,
                tvRegLog
            )
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edLoginEmail.isEnabled = !isLoading
            edLoginPassword.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            if (isLoading) {
                progressBarLayout.loadAnim(true)
            } else {
                progressBarLayout.loadAnim(false)
            }
        }
    }
}