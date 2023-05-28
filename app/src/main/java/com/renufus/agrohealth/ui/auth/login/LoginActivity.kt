package com.renufus.agrohealth.ui.auth.login

import android.os.Bundle
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
    }

    private fun login() {
        val email = binding.textInputEditTextLoginEmail.text.toString()
        val password = binding.textInputEditTextLoginPassword.text.toString()

        viewModel.login(email, password)
    }

    override fun onBackPressed() {
        utility.moveToAnotherActivity(this, MainActivity::class.java)
        finishAffinity()
    }
}
