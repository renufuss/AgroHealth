package com.renufus.agrohealth.ui.auth.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.databinding.ActivityRegisterBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val registerModule = module {
    factory { RegisterActivity() }
}

class RegisterActivity : AppCompatActivity() {
    val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    val utility = GeneralUtility()
    val viewModel: RegisterViewModel by viewModel<RegisterViewModel>()

    private val registerFunction = RegisterFunction(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        registerFunction.setTextColor()
        setAction()

        binding.buttonRegisterArrow.setOnClickListener {
            registerFunction.register()
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
}
