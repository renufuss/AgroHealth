package com.renufus.agrohealth.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.R
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.ui.main.MainActivity
import com.renufus.agrohealth.utility.GeneralUtility
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val splashModule = module {
    factory { SplashActivity() }
}

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val utility = GeneralUtility()
    private val viewModel: SplashViewModel by viewModel<SplashViewModel>()
    private var isLogin: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        isLogin = viewModel.userPreferences.getStatusLogin()

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLogin as Boolean) {
                utility.moveToAnotherActivity(this@SplashActivity, MainActivity::class.java)
            } else {
                utility.moveToAnotherActivity(this@SplashActivity, LoginActivity::class.java)
            }
            finish()
        }, 1200)
    }
}
