package com.renufus.agrohealth.ui.auth.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.renufus.agrohealth.R
import com.renufus.agrohealth.databinding.ActivityRegisterBinding
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setTextColor()
        setAction()
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
