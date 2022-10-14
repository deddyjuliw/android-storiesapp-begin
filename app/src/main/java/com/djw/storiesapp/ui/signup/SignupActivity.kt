package com.djw.storiesapp.ui.signup

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
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.djw.storiesapp.R
import com.djw.storiesapp.databinding.ActivitySignupBinding
import com.djw.storiesapp.ui.UserViewModelFactory
import com.djw.storiesapp.data.Result
import com.djw.storiesapp.loadAnim
import com.djw.storiesapp.ui.signin.SigninActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        signupViewModel = ViewModelProvider(this, factory).get(
            SignupViewModel::class.java
        )
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.edRegisterName.error = resources.getString(R.string.input_message, "Name")
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error = resources.getString(R.string.input_message, "Email")
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.error = resources.getString(R.string.input_message, "Password")
                }
                else -> {
                    signupViewModel.register(name, email, password).observe(this) { result ->
                        if (result != null) {
                            when(result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    val data = result.data
                                    if (data.error) {
                                        Toast.makeText(this@SignupActivity, data.message, Toast.LENGTH_LONG).show()
                                    }
                                    else {
                                        AlertDialog.Builder(this).apply {
                                            setTitle(resources.getString(R.string.reg_title))
                                            setMessage(resources.getString(R.string.reg_message))
                                            setPositiveButton(resources.getString(R.string.reg_action)) { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, resources.getString(R.string.register_error), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.tvLoginReg.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvRegPage, View.ALPHA, 1f).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(binding.tvNameReg, View.ALPHA, 1f).setDuration(500)
        val edName = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmailReg, View.ALPHA, 1f).setDuration(500)
        val edEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPasswordReg, View.ALPHA, 1f).setDuration(500)
        val edPassword = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvTxtLogin, View.ALPHA, 1f).setDuration(500)
        val tvLoginReg = ObjectAnimator.ofFloat(binding.tvLoginReg, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                tvName,
                edName,
                tvEmail,
                edEmail,
                tvPassword,
                edPassword,
                btnRegister,
                tvLogin,
                tvLoginReg
            )
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edRegisterName.isEnabled = !isLoading
            edRegisterEmail.isEnabled = !isLoading
            edRegisterPassword.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

            if (isLoading) {
                progressBarLayout.loadAnim(true)
            } else {
                progressBarLayout.loadAnim(false)
            }
        }
    }
}