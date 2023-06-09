package com.renufus.agrohealth.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.R
import com.renufus.agrohealth.ui.auth.login.LoginActivity
import com.renufus.agrohealth.utility.GeneralUtility

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val utility = GeneralUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            utility.moveToAnotherActivity(this@SplashActivity, LoginActivity::class.java)
            finish()
        }, 1200)
    }
}
