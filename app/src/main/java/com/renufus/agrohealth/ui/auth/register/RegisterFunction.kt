package com.renufus.agrohealth.ui.auth.register

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.renufus.agrohealth.R
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegisterFunction(private val activity: RegisterActivity) {

    fun register() {
        val email = activity.binding.textInputEditTextRegisterEmail.text.toString()
        val username = activity.binding.textInputEditTextRegisterUsername.text.toString()
        val password = activity.binding.textInputEditTextRegisterPassword.text.toString()
        val confirmPassword = activity.binding.textInputEditTextRegisterConfirmPassword.text.toString()

        val registerValidation = registerValidation(email, username, password, confirmPassword)

        if (registerValidation) {
            showLoading(true)
            activity.viewModel.register(email, username, password)

            activity.viewModel.errorStatus.observe(activity) { error ->
                if (!error) {
                    activity.binding.textViewRegisterErrorText.visibility = View.GONE
                    MotionToast.createToast(
                        activity,
                        "Success",
                        "You have successfully registered",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(activity, www.sanju.motiontoast.R.font.helvetica_regular),
                    )
                    activity.utility.moveToAnotherActivity(activity, LoginActivity::class.java)
                    activity.finish()
                } else {
                    activity.viewModel.errorMessage.observe(activity) { response ->
                        activity.binding.textViewRegisterErrorText.visibility = View.VISIBLE
                        activity.binding.textViewRegisterErrorText.text = response
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    showLoading(false)
                }, activity.utility.delayLoading)
            }
        }

        return
    }

    fun setTextColor() {
        var fullText: String?
        var coloredTextList: List<Pair<String, Int>>?

        fullText = "Already Member?"
        coloredTextList = listOf(
            Pair("Already", ContextCompat.getColor(activity, R.color.brown)),
            Pair("Member?", ContextCompat.getColor(activity, R.color.green)),
        )
        activity.utility.setColoredText(activity.binding.textViewRegisterAlreadyMember, fullText, coloredTextList)

        fullText = "Create Account"
        coloredTextList = listOf(
            Pair("Create", ContextCompat.getColor(activity, R.color.gray)),
            Pair("Account", ContextCompat.getColor(activity, R.color.green)),
        )
        activity.utility.setColoredText(activity.binding.textViewRegisterCreateAccount, fullText, coloredTextList)
    }

    private fun registerValidation(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        activity.binding.textViewRegisterErrorText.visibility = View.GONE
        removeError()

        val isValidEmail: Boolean = activity.utility.validationEmail(email)
        val isInvalid =
            (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || password.length < 8 || password != confirmPassword || !isValidEmail)

        if (isInvalid) {
            if (email.isEmpty()) {
                activity.binding.textInputLayoutRegisterEmail.helperText = "Email is required"
            } else {
                activity.binding.textInputLayoutRegisterEmail.helperText = ""
            }

            if (username.isEmpty()) {
                activity.binding.textInputLayoutRegisterUsername.helperText = "Username is required"
            } else {
                activity.binding.textInputLayoutRegisterUsername.helperText = ""
            }

            if (password.isEmpty()) {
                activity.binding.textInputLayoutRegisterPassword.helperText = "Password is required"
            } else {
                activity.binding.textInputLayoutRegisterPassword.helperText = ""
            }

            if (confirmPassword.isEmpty()) {
                activity.binding.textInputLayoutRegisterConfirmPassword.helperText = "Confirm  your password"
            } else {
                activity.binding.textInputLayoutRegisterConfirmPassword.helperText = ""
            }

            val isPasswordNotEmpty: Boolean = password.isNotEmpty() && confirmPassword.isNotEmpty()
            if (isPasswordNotEmpty) {
                if (password != confirmPassword) {
                    activity.binding.textInputLayoutRegisterPassword.helperText = "Passwords doesn't match"
                    activity.binding.textInputLayoutRegisterConfirmPassword.helperText =
                        "Passwords doesn't match"
                } else {
                    activity.binding.textInputLayoutRegisterPassword.helperText = ""
                    activity.binding.textInputLayoutRegisterConfirmPassword.helperText = ""
                }
            }

            if (password.isNotEmpty() && password.length < 8) {
                activity.binding.textInputLayoutRegisterPassword.helperText =
                    "Password must be longer than 8 characters"
            } else {
                activity.binding.textInputLayoutRegisterPassword.helperText = ""
            }

            if (email.isNotEmpty()) {
                if (!isValidEmail) {
                    activity.binding.textInputLayoutRegisterEmail.helperText = "Your email is invalid"
                } else {
                    activity.binding.textInputLayoutRegisterEmail.helperText = ""
                }
            }
            return false
        }

        return true
    }

    private fun removeError() {
        activity.binding.textInputLayoutRegisterEmail.helperText = ""
        activity.binding.textInputLayoutRegisterUsername.helperText = ""
        activity.binding.textInputLayoutRegisterPassword.helperText = ""
        activity.binding.textInputLayoutRegisterConfirmPassword.helperText = ""
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> {
                activity.binding.progressBarRegisterLoading.visibility = View.VISIBLE
                activity.binding.buttonRegisterArrow.isEnabled = false
                activity.binding.buttonRegisterArrow.isClickable = false
            }

            else -> {
                activity.binding.progressBarRegisterLoading.visibility = View.GONE
                activity.binding.buttonRegisterArrow.isEnabled = true
                activity.binding.buttonRegisterArrow.isClickable = true
            }
        }
    }
}
