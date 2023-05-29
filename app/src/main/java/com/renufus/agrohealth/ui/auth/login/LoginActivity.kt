package com.renufus.agrohealth.ui.auth.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.databinding.ActivityLoginBinding
import com.renufus.agrohealth.ui.auth.register.RegisterActivity
import com.renufus.agrohealth.ui.main.MainActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val loginModule = module {
    factory { LoginActivity() }
}

class LoginActivity() : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()
    private val viewModel: LoginViewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setAction()
    }

    private fun setAction() {
        binding.textViewLoginSignup.setOnClickListener {
            utility.moveToAnotherActivity(this@LoginActivity, RegisterActivity::class.java)
        }

        binding.textViewLoginNewUser.setOnClickListener {
            utility.moveToAnotherActivity(this@LoginActivity, RegisterActivity::class.java)
        }

        binding.buttonLoginArrow.setOnClickListener {
            login()
        }
    }

    private fun loginValidation(email: String, password: String): Boolean {
        removeError()

        val isValidEmail: Boolean = utility.validationEmail(email)
        val isInvalid = (email.isEmpty() || password.isEmpty() || !isValidEmail)
        if (isInvalid) {
            if (email.isEmpty()) {
                binding.textInputLayoutLoginEmail.helperText = "Email is required"
            } else {
                binding.textInputLayoutLoginEmail.helperText = ""
            }

            if (password.isEmpty()) {
                binding.textInputLayoutLoginPassword.helperText = "Password is required"
            } else {
                binding.textInputLayoutLoginPassword.helperText = ""
            }

            if (email.isNotEmpty()) {
                if (!isValidEmail) {
                    binding.textInputLayoutLoginEmail.helperText = "Your email is invalid"
                } else {
                    binding.textInputLayoutLoginEmail.helperText = ""
                }
            }

            return false
        }
        return true
    }

    private fun removeError() {
        binding.textInputLayoutLoginEmail.helperText = ""
        binding.textInputLayoutLoginPassword.helperText = ""
    }

    private fun login() {
        val email = binding.textInputEditTextLoginEmail.text.toString()
        val password = binding.textInputEditTextLoginPassword.text.toString()

        val loginValidation = loginValidation(email, password)

        if (loginValidation) {
            showLoading(true)

            viewModel.login(email, password)

            viewModel.errorStatus.observe(this) { error ->
                if (!error) {
                    viewModel.login.observe(this) { data ->
                        val token = data.user[0].token
                        viewModel.userPreferences.setToken(token)
                        viewModel.userPreferences.setStatusLogin(true)
                        viewModel.userPreferences.setEmail(email)
                        viewModel.userPreferences.setUsername(data.user[0].username)

                        utility.moveToAnotherActivity(this@LoginActivity, MainActivity::class.java)
                        finishAffinity()
                    }
                } else {
                    viewModel.errorMessage.observe(this) {
                        Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    showLoading(false)
                }, utility.delayLoading)
            }
        }

        return
    }
    override fun onBackPressed() {
        utility.moveToAnotherActivity(this, MainActivity::class.java)
        finishAffinity()
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressBarLoginLoading.visibility = View.VISIBLE
                binding.buttonLoginArrow.isEnabled = false
                binding.buttonLoginArrow.isClickable = false
            }

            else -> {
                binding.progressBarLoginLoading.visibility = View.GONE
                binding.buttonLoginArrow.isEnabled = true
                binding.buttonLoginArrow.isClickable = true
            }
        }
    }
}
