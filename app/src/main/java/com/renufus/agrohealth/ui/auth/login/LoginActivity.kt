package com.renufus.agrohealth.ui.auth.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.databinding.ActivityLoginBinding
import com.renufus.agrohealth.ui.main.MainActivity
import com.renufus.agrohealth.ui.auth.register.RegisterActivity
import com.renufus.agrohealth.utility.GeneralUtility

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()

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

    override fun onBackPressed() {
        utility.moveToAnotherActivity(this, MainActivity::class.java)
        finishAffinity()
    }
}
