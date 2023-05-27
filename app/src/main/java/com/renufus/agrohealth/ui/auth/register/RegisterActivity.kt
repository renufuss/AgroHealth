package com.renufus.agrohealth.ui.auth.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityRegisterBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val registerModule = module {
    factory { RegisterActivity() }
}

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()
    private val viewModel: RegisterViewModel by viewModel<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setTextColor()
        setAction()

        binding.buttonRegisterArrow.setOnClickListener {
            register()
        }
    }

    private fun registerValidation(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        val isInvalid =
            (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || password.length < 8 || password != confirmPassword)

        if (isInvalid) {
            if (email.isEmpty()) {
                binding.textInputLayoutRegisterEmail.helperText = "Email is required"
            } else {
                binding.textInputLayoutRegisterEmail.helperText = ""
            }

            if (username.isEmpty()) {
                binding.textInputLayoutRegisterUsername.helperText = "Username is required"
            } else {
                binding.textInputLayoutRegisterUsername.helperText = ""
            }

            if (password.isEmpty()) {
                binding.textInputLayoutRegisterPassword.helperText = "Password is required"
            } else {
                binding.textInputLayoutRegisterPassword.helperText = ""
            }

            if (confirmPassword.isEmpty()) {
                binding.textInputLayoutRegisterConfirmPassword.helperText = "Confirm  your password"
            } else {
                binding.textInputLayoutRegisterConfirmPassword.helperText = ""
            }

            if (password.length < 8) {
                binding.textInputLayoutRegisterPassword.helperText =
                    "Password must be longer than 8 characters"
            } else {
                binding.textInputLayoutRegisterPassword.helperText = ""
            }

            val isPasswordNotEmpty: Boolean = password.isNotEmpty() && confirmPassword.isNotEmpty()
            if (isPasswordNotEmpty) {
                if (password.equals(confirmPassword)) {
                    binding.textInputLayoutRegisterPassword.helperText = "Passwords doesn't match"
                    binding.textInputLayoutRegisterConfirmPassword.helperText = "Passwords doesn't match"
                } else {
                    binding.textInputLayoutRegisterPassword.helperText = ""
                    binding.textInputLayoutRegisterConfirmPassword.helperText = ""
                }
            }

            if (email.isNotEmpty()) {
                val isValidEmail: Boolean = utility.validationEmail(email)
                if (!isValidEmail) {
                    binding.textInputLayoutRegisterEmail.helperText = "Your email is invalid"
                } else {
                    binding.textInputLayoutRegisterEmail.helperText = ""
                }
            }
            return false
        }

        return true
    }

    private fun register() {
        val email = binding.textInputEditTextRegisterEmail.text.toString()
        val username = binding.textInputEditTextRegisterUsername.text.toString()
        val password = binding.textInputEditTextRegisterPassword.text.toString()
        val confirmPassword = binding.textInputEditTextRegisterConfirmPassword.text.toString()

        val registerValidation = registerValidation(email, username, password, confirmPassword)

        if (registerValidation) {
            viewModel.register(email, username, password)

            showLoading(true)

            viewModel.errorStatus.observe(this) {
                if (it == false) {
                    Toast.makeText(this, "You have successfully registered", Toast.LENGTH_SHORT)
                        .show()
                    utility.moveToAnotherActivity(this@RegisterActivity, LoginActivity::class.java)
                    finish()
                } else {
                    Toast.makeText(this, "An unknown error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return
    }

    fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.progressBarRegisterLoading.visibility = View.VISIBLE
                binding.buttonRegisterArrow.isEnabled = false
                binding.buttonRegisterArrow.isClickable = false
            }

            else -> {
                binding.progressBarRegisterLoading.visibility = View.GONE
                binding.buttonRegisterArrow.isEnabled = true
                binding.buttonRegisterArrow.isClickable = true
            }
        }
    }

    private fun setAction() {
        binding.textViewRegisterLogin.setOnClickListener {
            utility.moveToAnotherActivity(this@RegisterActivity, LoginActivity::class.java)
            finish()
        }

        binding.textViewRegisterAlreadyMember.setOnClickListener {
            utility.moveToAnotherActivity(this@RegisterActivity, LoginActivity::class.java)
            finish()
        }
    }

    private fun setTextColor() {
        var fullText: String?
        var coloredTextList: List<Pair<String, Int>>?

        fullText = "Already Member?"
        coloredTextList = listOf(
            Pair("Already", ContextCompat.getColor(this, R.color.brown)),
            Pair("Member?", ContextCompat.getColor(this, R.color.green)),
        )
        utility.setColoredText(binding.textViewRegisterAlreadyMember, fullText, coloredTextList)

        fullText = "Create Account"
        coloredTextList = listOf(
            Pair("Create", ContextCompat.getColor(this, R.color.gray)),
            Pair("Account", ContextCompat.getColor(this, R.color.green)),
        )
        utility.setColoredText(binding.textViewRegisterCreateAccount, fullText, coloredTextList)
    }
}
