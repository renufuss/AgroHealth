package com.renufus.agrohealth.ui.auth.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityRegisterBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

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
        binding.textViewRegisterErrorText.visibility = View.GONE
        removeError()

        val isValidEmail: Boolean = utility.validationEmail(email)
        val isInvalid =
            (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || password.length < 8 || password != confirmPassword || !isValidEmail)

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

            val isPasswordNotEmpty: Boolean = password.isNotEmpty() && confirmPassword.isNotEmpty()
            if (isPasswordNotEmpty) {
                if (password != confirmPassword) {
                    binding.textInputLayoutRegisterPassword.helperText = "Passwords doesn't match"
                    binding.textInputLayoutRegisterConfirmPassword.helperText =
                        "Passwords doesn't match"
                } else {
                    binding.textInputLayoutRegisterPassword.helperText = ""
                    binding.textInputLayoutRegisterConfirmPassword.helperText = ""
                }
            }

            if (password.isNotEmpty() && password.length < 8) {
                binding.textInputLayoutRegisterPassword.helperText =
                    "Password must be longer than 8 characters"
            } else {
                binding.textInputLayoutRegisterPassword.helperText = ""
            }

            if (email.isNotEmpty()) {
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

    private fun removeError() {
        binding.textInputLayoutRegisterEmail.helperText = ""
        binding.textInputLayoutRegisterUsername.helperText = ""
        binding.textInputLayoutRegisterPassword.helperText = ""
        binding.textInputLayoutRegisterConfirmPassword.helperText = ""
    }

    private fun register() {
        val email = binding.textInputEditTextRegisterEmail.text.toString()
        val username = binding.textInputEditTextRegisterUsername.text.toString()
        val password = binding.textInputEditTextRegisterPassword.text.toString()
        val confirmPassword = binding.textInputEditTextRegisterConfirmPassword.text.toString()

        val registerValidation = registerValidation(email, username, password, confirmPassword)

        if (registerValidation) {
            showLoading(true)
            viewModel.register(email, username, password)

            viewModel.errorStatus.observe(this) { error ->
                if (!error) {
                    binding.textViewRegisterErrorText.visibility = View.GONE
                    MotionToast.createToast(
                        this,
                        "Success",
                        "You have successfully registered",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular),
                    )
                    utility.moveToAnotherActivity(this@RegisterActivity, LoginActivity::class.java)
                    finish()
                } else {
                    viewModel.errorMessage.observe(this) { response ->
                        binding.textViewRegisterErrorText.visibility = View.VISIBLE
                        binding.textViewRegisterErrorText.text = response
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    showLoading(false)
                }, utility.delayLoading)
            }
        }

        return
    }

    private fun showLoading(loading: Boolean) {
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
